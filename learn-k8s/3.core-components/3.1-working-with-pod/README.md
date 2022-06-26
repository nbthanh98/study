## **Working with pod.**

![](../../2.kubernetes-architecture/images/2022-05-29_23-27.png)

### **1.1 Introducing pods**

Trong Docker thì `container` được coi là đơn vị nhỏ nhất, còn với Kubernetes thì `Pod` được coi là đơn vị nhỏ nhất. Bên trong `Pod` thì có thể chứa một hoặc nhiều `containers`, các containers bên trong Pod sẽ chia sẻ storage/network. Bên trong Pod sẽ mô tả cách để chạy các containers được định nghĩa bên trong Pod (image là gì?, mount volume?, port?,..).

### **1.2 Understanding why we need pods**

- Tại sao chúng tạ lại cần đến `Pod` ?
- Taị sao không chạy luôn các `containers` trực tiếp, mà lại cần `Pod` wrap các `containers` ?

**1.2.1 Tại sao chạy nhiều containers (mỗi container chạy một process) lại tốt hơn là chạy nhiều process trong một containers.**

![](images/2022-06-15_00-04.png)

Hình trên là mình đang có 2 containers.  `Main container`(chạy application) và `Supporting container`(sicar hoặc một containers xử lý logs,..)

Containers thường sẽ chỉ chạy một process duy nhất PID = 1 (trừ khi bản thân process này sinh ra các process con). Nếu trong trường hợp mà chúng ra gộp 2 `containers` trên thành một `container` chạy 2 process (một process cho application, 1 process cho support-container), sẽ xảy ra trường hợp như sau:

- Trong trường hợp application bị crash thì container chứa 2 process bị restart lại, nếu mà tách ra thành 2 container thì sẽ chỉ phải restart `main-container` chạy application thôi, không phải restart `support-container` và ngược lại.

- Trong trường hợp mà `support-container` là chạy container xứ lý logs, thì khi `main-container` bị crash thì có thể `support-container` vẫn có thể logs lại vì sao `main-container` bị crash.

- Chạy mỗi một process một container cũng đảm bảo được mỗi một container chỉ làm một nghiệp vụ duy nhất thôi.

**Tóm lại** : vì những lý do trên thì hãy chạy mỗi một container một process.

**1.2.2 Tại sao chúng tạ lại cần đến Pod ?**

Vì trong thực tế chúng ta cũng cần chạy 2 containers (container chạy application và support container). Support container có thể là (side-car container trong service-mesh hoặc là container để xử lý logs,..). Nhưng vì những lý do phía trên khi chạy 2 process trong 1 container, nên mình nghĩ rằng Kubernetes mới đẻ ra thêm một ông là `Pod`, ông `Pod` này sẽ wrap 1 hoặc nhiều container và deploy trên 1 Kubernetes. Các containers trong một Pod sẽ chia sẻ `Pod environment` (This includes things like IPC namespace, shared memory, volumes, network stack and more).

### **1.3 Define Pod**

Pod được define trong file yaml, vd dưới thì đang chạy 1 container image: `nginx:1.14.2`.
```yaml
apiVersion: v1
kind: Pod # Pod là một loại Object trong Kubernets.
metadata: # Chứa những mô tả thêm về Pod, VD: tên, label,..
  name: nginx

# Phần này chứa các thông tin của Pod, chạy bao nhiêu containers?, 
# image  là gì?, port của container,..
spec:
  containers: 
  - name: nginx
    image: nginx:1.14.2
    ports:
    - containerPort: 80
```
### **1.4 Cách mà Kubernetes tạo Pod**

![](images/9.png)

- (1) Đầu tiên là cần define Pod YAML manifest, thực hiện gửi POST request đến `API Server`, lúc này `API Server` sẽ thực hiện validate YAML file, authen và author user đang tương tác với cluster (check xem user này có được tương tác với loại resource hay không?).
- (2) Sau khi `API-Server` validate, authen, author thì sẽ lưu content pod manifest vào `etcd` (một loại data store, key-value, lưu trạng thái `desired state` của cluster ).
- (3) `Etcd` sẽ trả về response cho `API Server` rằng có lưu thành công hay không?
- (4) `API Server` sẽ trả về cho `User` rằng đã created `Pod`. VD: `pod/mc1 created`
- `Scheduler` sẽ luôn hỏi `API Server` rằng có cái workload gì cần deploy hay không? (mỗi 5s). Và rồi `Scheduler` đã có task là deploy một cái `Pod`. Nhiệm vụ của `Scheduler` sẽ là chọn ra một `Worker Node` phù hợp nhất để deploy Pod dựa vào một số các tiêu trí như (Node healthy, cpu, memoty,..).
- (5) Sau khi được một `Worker Node` vd là `Worker-node-2` để deploy Pod. `Scheduler` sẽ không trực tiếp deploy Pod lên `worker-node-2` mà sẽ report lại cho `API Server` rằng "À cái Pod kia nên được deploy ở trên worker-node-2 đó :D".
- (6) Sau khi được `Scheduler` report là nên deploy Pod ở `Worker-node-2` thì `API Server` cũng sẽ không deploy Pod ngay mà sẽ thực hiện update lại Object Pod ở bước (2).
- (7) `Etcd` sẽ trả về response cho `API Server` rằng có lưu thành công hay không?
- (8) Lúc này thì `API Server` sẽ giao tiếp với `kubelet` để thực hiên Deploy Pod trên `Worker-node-2`.
- (9) `Kubelet` cũng sẽ không trực tiếp deploy Pod và nó sẽ hướng dẫn cho `Container runtime` cách để có thể deploy Pod và report lại trạng thái của Pod cho `API-Server`.
   ```
   Events:
   Type    Reason     Age   From               Message
   ----    ------     ----  ----               -------
   Normal  Scheduled  6s    default-scheduler  Successfully assigned default/mc1 to nbt
   Normal  Pulling    6s    kubelet            Pulling image "nginx"
   Normal  Pulled     4s    kubelet            Successfully pulled image "nginx" in 2.214132838s
   Normal  Created    4s    kubelet            Created container 1st
   Normal  Started    4s    kubelet            Started container 1st
   Normal  Pulling    4s    kubelet            Pulling image "debian"
   Normal  Pulled     1s    kubelet            Successfully pulled image "debian" in 2.202111209s
   Normal  Created    1s    kubelet            Created container 2nd
   Normal  Started    1s    kubelet            Started container 2nd
   ```
   ```
   kubectl apply -f single-container-pod.yaml

   # Kiểm tra trạng thái của Pod:
   kubectl get po
   NAME    READY   STATUS              RESTARTS   AGE
   nginx   0/1     ContainerCreating   0          8s
   
   # Pod sau khi deploy thành công:
   kubectl get po
   NAME    READY   STATUS    RESTARTS   AGE
   nginx   1/1     Running   0          11s
  ```

### **1.5 Pod lifecycle**

![](images/7.png)

### **1.7 Chạy nhiều containers trong một Pod**

1. **Introduction**

    - Trong những trường hợp bình thường thì vẫn chạy một container trong một Pod, nhưng cũng sẽ có những design patterns mà yêu cầu mình chạy nhiều container trong một Pod, VD:  Sidecar pattern, Adaptor pattern, and Ambassador pattern.

        - **Sidecar pattern**

            ![](images/3.png)

            Chúng ta sẽ có 1 container gọi là `Main container`(container chạy application) và thêm một container nữa gọi là `Support container` (sẽ xử lý các task như: logs, monitoring,..) tách biệt khỏi `main container`.
    
    - Những thứ có thể làm nếu chạy nhiều containers trong Pod:
        
        - Các containers trong cùng một Pod có thể tương tác với nhau thông qua `Network namespace` và `share volume`. Tất cả các containers trong cùng một Pod thì sẽ chia sẻ `Network namespace`, có nghĩa là các containers có thể gọi nhau thông qua `localhost`.
        - Tất cả các containers sẽ có cùng lifecicle, sẽ chạy cùng trên một Node.

2. **Demo**

    - **Demo Container Shared Network Namespace**
    
        Tất cả containers trong một Pod sẽ chia sẻ `network namespace` có nghĩa là: tất cả các containers đều có thể gọi nhau thông qua `localhost`.

        ![](images/4.png)

        ```yaml
        apiVersion: v1
        kind: Pod
        metadata:
          name: nginx
        spec:
          containers:

          - name: nginx-1
            image: nginx:1.14.2
            ports:
            - containerPort: 80

          - name: busybox
            image: progrium/busybox
            args:
            - sleep
            - "1000000"
        ```
        ```
        kubectl apply -f multiple-container-pod.yaml 
        pod/nginx created

        kubectl get po
        NAME    READY   STATUS    RESTARTS   AGE
        nginx   2/2     Running   0          17s

        # Truy cập vào pod `nginx` và container busybox
        kubectl exec -it po/nginx -c busybox sh

        # Cài curl để thực hiện test.
        opkg-install curl

        # curl đến container nginx bằng localhost.
        / # curl http://localhost:80
        <!DOCTYPE html>
        <html>
        <head>
        <title>Welcome to nginx!</title>
        <style>
            body {
                width: 35em;
                margin: 0 auto;
                font-family: Tahoma, Verdana, Arial, sans-serif;
            }
        </style>
        </head>
        <body>
        <h1>Welcome to nginx!</h1>
        <p>If you see this page, the nginx web server is successfully installed and
        working. Further configuration is required.</p>

        <p>For online documentation and support please refer to
        <a href="http://nginx.org/">nginx.org</a>.<br/>
        Commercial support is available at
        <a href="http://nginx.com/">nginx.com</a>.</p>

        <p><em>Thank you for using nginx.</em></p>
        </body>
        </html>
        ```
    - **Demo containers share volumes**
      
      Các containers trong cùng một Pod sẽ chia sẻ Volume, volume này sẽ có linetime giống với Pod, điều này có nghĩa là Volume này sẽ chỉ tồn tại khi Pod còn tồn tại, nếu Pod bị xóa đi vì một lý do nào đó thì Volume cũng sẽ bị xóa, khi Pod được tạo lại thì volume cũng sẽ được tạo mới.

      ```yaml
      apiVersion: v1
      kind: Pod
      metadata:
        name: mc1
      spec:
        containers:
        - name: 1st
          image: nginx
          volumeMounts:
          - name: html
            mountPath: /usr/share/nginx/html
        - name: 2nd
          image: debian
          volumeMounts:
          - name: html
            mountPath: /share-volume
          command: ["/bin/sh", "-c"]
          args:
            - while true; do
                mkdir -p /share-volume/;
                echo "Container 2 write $(date +%F_%H-%M-%S)">> /share-volume/index.html;
                sleep 10000;
              done
        volumes:
        - name: html
          emptyDir: {}
      ```
      ![](images/5.png)

      File yaml trên, define một volume có tên là `html` và kiểu là `emptyDir` (`emptyDir` là loại volume sẽ tồn tại trong thời gian Pod tồn tại, volume này khi khởi tạo sẽ là một folder rỗng, volume này được tạo khi Pod được assign cho Node).

      Đầu tiên là chạy `container`: 1st (image: nginx:latest), container này sẽ dùng volume tên là `html` để mount vào folder `/usr/share/nginx/html` trong container. Containers thứ 2 tên `2nd` (image: debian:latest), container này dùng volume tên `html` để mount vào folder `/share-volume` trong container.

      Cả 2 containers `1st` và `2nd` đều dùng chung volume là `html` nên là đoan:
      ```
      args:
        - while true; do
            mkdir -p /share-volume/;
            echo "Container 2 write $(date +%F_%H-%M-%S)">> /share-volume/index.html;
            sleep 10000;
          done
      ```
      Mình có thắc mắc là vì sao mình chỉ tạo file `index.html` ở folder `/share-volume` và bên trong container `2nd` thôi nhưng bên trong container `1st` lại có file `index.html` ở folder `/usr/share/nginx/html` nhỉ?. Đó là vì cả 2 containers đều dùng chung 1 volume tên `html`, Khi container `2nd` tạo file `index.html` thì lúc này bên trong volume `html` cũng sẽ có file `index.html`, sau đó container `2nd` mount lại vào folder `/usr/share/nginx/html` thì container `2nd` đã có file `index.html`.

## Nguồn tham khảo:

- https://linchpiner.github.io/k8s-multi-container-pods.html

- https://medium.com/bb-tutorials-and-thoughts/understanding-multi-container-pods-12f5780f3956
