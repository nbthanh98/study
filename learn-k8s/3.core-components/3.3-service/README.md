# **#Services: enabling clients to discover and talk to pods**
## **1. Giới thiệu**
Ở những phần trước thì mình có tìm hiểu các tạo Pod, sử dụng ReplicationController và ReplicaSet để quản lý lifeCycle của Pod. Trong một hệ thống (VD: Microservice) các services sẽ phải tương tác với các service khác bên trong Cluster, hoặc có thể là nhận request từ bên ngoài Cluster. Pod sẽ được assigned một IP khi scheduled vào một node nào đó, có thể access Pod thông qua IP và Port của Pod (chỉ có thể truy cập bên trong Cluster).

![](../3.2-Replication-and-ther-controllers/images/1.png)

```shell
# Lấy IP của Pods
kubectl get po -o wide
NAME            READY   STATUS    RESTARTS      AGE   IP            NODE   NOMINATED NODE   READINESS GATES
demo-rs-jgdq2   1/1     Running   2 (76s ago)   38h   10.1.28.114   nbt    <none>           <none>
demo-rs-f2dmw   1/1     Running   2 (76s ago)   38h   10.1.28.102   nbt    <none>           <none>
demo-rs-xrwnl   1/1     Running   2 (76s ago)   38h   10.1.28.109   nbt    <none>           <none>

# Truy cập Pod thông qua IP và Port.
curl 10.1.28.114:8080/healCheck/getStatus1
Service Running
```
Nhưng sẽ có những vấn đề như sau:

1. Mỗi khi Pod bị xóa và tạo lại thì Pod sẽ được assign một IP mới. Nên nếu mà fix cứng IP của Pod để gọi thì sẽ bị sai nếu Pod bị xóa đi và tạo lại.
2. Một application có thể được chạy bởi nhiều instance (1 instance = 1 Pod), application có thể scale up và scale down, mỗi Pod lại được assign 1 IP riêng. Client thì không nên cần quan tâm về một số lượng Pod và IP của chúng. Thay vì đó nên có phải có một thứ gì đó tạm gọi là `X` đứng trước các Pod và Client chỉ quan tâm đến IP của `X` mà không cần quan tâm đến số lượng Pod và IP của Pod.

Để giải quyết tất cả những vấn đề trên thì Kubernetes giới thiệu một loại Object đó là `Service`.

## **2. Giới thiệu về Service**
Service là một loại Object của Kubernetes, service sẽ có một IP và và Port sẽ không thay đổi khi service vẫn còn tồn tại. Clients có thể gọi đến các application thông qua IP và Port của Service, các requests này sẽ được Service router đến các Pods đứng sau service. Bằng các này thì Client sẽ không cần quan tâm đến IP của Pod, vị trí của Pod (các Pods có thể được deploy ở đâu đó trên Cluster), Client chỉ cần quan tâm đến service thôi.

![](./images/1.png)

**Giải thích:**

Hình trên có 2 components là `front-end` (chạy 3 Pod) và `back-end` (chạy 1 Pod). Bằng việc tạo một service cho frontend pods và configs cái service này để nó có thể được access từ bên ngoài cluster, request sẽ được route vào 3 pods của frontend. Tương tự như vậy thì cũng tạo một service cho backend. Frontend có thể gọi đến backend service thông qua tên service. IP của frontend service và backend service sẽ không thay đổi nếu như 2 service này vẫn còn tồn tại.

## **3. Khởi tạo Service**

```yaml
apiVersion: v1
kind: Service                  # Loại Object là: `Service` 
metadata:                      
  name: service-1              # Tên của service: service-1
  namespace: default           # Service được deploy ở namespace default.
spec:
  selector:
    app: label-pod             # labels selector: `app: label-pod` cần match với labels của Pod.
  type: ClusterIP              # Kiểu của Service là: ClusterIP (chỉ có thể gọi được trong nội bộ Cluster).
  ports:
  - port: 80                   # Port của service: 80
    targetPort: 8080           # Port của containers mà service sẽ forward request vào.
```

Ở trên là đang định nghĩa một `Service` có tên là `service-1`, service này được deploy ở namespace default. Service này sẽ có port là 80, khi có request đến service này thì các request sẽ được route đến các Pod có labels match với labels của service là: `app: label-pod`  

**Demo**

1. Gọi các API của application thông qua IP và Port của Service (Bên trong Cluster).
2. Gọi các API của application thông qua serviceName của Service (Bên trong Cluster).


    ```shell
    kubectl apply -f 1.replicaSet.yaml 
    replicaset.apps/demo-rs create

    kubectl get po -o wide --show-labels
    NAME                  READY   STATUS    RESTARTS   AGE     IP            NODE   NOMINATED NODE   READINESS GATES   LABELS
    demo-rs-p24dm         1/1     Running   0          40m     10.1.28.110   nbt    <none>           <none>            app=label-pod
    demo-rs-hmrbg         1/1     Running   0          40m     10.1.28.112   nbt    <none>           <none>            app=label-pod
    demo-rs-tqtc5         1/1     Running   0          40m     10.1.28.103   nbt    <none>           <none>            app=label-pod

    --------------------------------------------------------------------------------------------------------------------------------

    kubectl apply -f 2.service.yaml 
    service/service-1 created

    kubectl get svc
    NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)   AGE
    service-1    ClusterIP   10.152.183.58   <none>        80/TCP    4s

    --------------------------------------------------------------------------------------------------------------------------------
    # Get endpoint
    kubectl get ep
    NAME         ENDPOINTS                                            AGE
    kubernetes   192.168.1.123:16443                                  13d
    service-1    10.1.28.103:8080,10.1.28.110:8080,10.1.28.112:8080   33m

    #10.1.28.103:8080,10.1.28.110:8080,10.1.28.112:8080 giống với 3 IP và Port của Pod.

    --------------------------------------------------------------------------------------------------------------------------------

    # Deploy một Pod nữa cho việc test.
    kubectl apply -f pod-for-test.yaml 
    pod/pod-for-test created

    kubectl get po
    pod-for-test          1/1     Running   0          3s

    # Truy cập vào Pod
    kubectl exec -it po/pod-for-test sh
    kubectl exec [POD] [COMMAND] is DEPRECATED and will be removed in a future version. Use kubectl exec [POD] -- [COMMAND] instead.
    / # 
    / # 
    / # 

    # chạy 3 lệnh dưới để cài curl vào container bên trong pod-for-test.
    apk update
    apk upgrade
    apk add curl

    # Gửi request đến service thông qua IP và Port của service. request này sẽ được forward đến 3 Pod đứng sau service.
    / # curl 10.152.183.58:80/healCheck/getStatus1
    Service Running/ # 


    # Gửi request đến service thông qua serviceName của service. request này sẽ được forward đến 3 Pod đứng sau service.
    / # curl http://service-1/healCheck/getStatus1
    Service Running/ # 
    / # 
    ```

3. Gọi các API của application thông qua IP và Port của Service (Bên ngoài Cluster). 
    ```shell
    apiVersion: v1
    kind: Service
    metadata:
      name: service-2
      namespace: default
    spec:
      selector:
        app: label-pod

      # type=NodePort: giúp cho service có thể truy cập từ bên ngoài Cluster. 
      # Port sẽ được random trong khoảng 30000 -> 32000 nếu không mô tả một nodePort nào cụ thể thông qua trường `nodePort`.
      # nodePort: 30001  

      type: NodePort 
      ports:
      - port: 80
        targetPort: 8080

    kubectl get ep
    NAME         ENDPOINTS                                            AGE
    kubernetes   192.168.1.123:16443                                  13d
    service-2    10.1.28.103:8080,10.1.28.110:8080,10.1.28.112:8080   9m19s
    ```
    ![](./images/2.png)

## **4. Discovering services**