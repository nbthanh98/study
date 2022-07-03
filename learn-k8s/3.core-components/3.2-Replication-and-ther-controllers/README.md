# Replication and other controllers: deploying managed pods

## **1. Giới thiệu**
Trong phần trước thì chúng ta đã biết Pod là đơn vị nhỏ nhất trong Kubernetes, học cách tạo Pod, chạy 1 hoặc nhiều containers trong một Pod, các containers trong cùng một Pod thì chia sẻ với nhau: network space, volumes,..

Pod thì chúng ta cần tạo, giám sát và quản lý bằng tay. Nhưng trong thực tế thì sẽ cần một thứ gì đó tự động giám sát, quản lý Pod tự động. Để làm những thứ như trên thì trong thực tế sẽ không tạo Pod một cách trực tiếp, mà sẽ tạo Pod thông qua `Replication Controllers` hoặc `Deployments`, lúc này việc tạo Pod và quản lý Pod sẽ do `Replication Controllers` hoặc `Deployments` làm. Trong phần này mình sẽ tìm hiểu cách mà Kubernetes check nếu container vẫn còn alive và restart container nếu bị faild và cách để quản lý các Pod.

## **2. Keeping pods healthy**

Một trong những lợi ích chính của việc sử dụng Kubernetes đó là Kubernetes có khả năng đảm bảo được các containers sẽ luôn running ở đâu đó trong cluster. Để làm điều này thì ở phần trước mình có tạo Pod, Pod sẽ được schedule vào một Node nào đó và running. Giả sử bạn tạo một Pod (2 containers), nhưng điều gì sẽ xảy ra nếu một trong 2 containers đó die? Hoặc là cả 2 containers đều die?

```
nbt@nbt:~$ kubectl get po
NAME    READY   STATUS    RESTARTS        AGE
nginx   2/2     Running   14 (3h6m ago)   4d22h
mc1     2/2     Running   13 (42s ago)    3d23h

# Thực hiện kill process của container `1st`
kubectl  exec -it po/mc1 -c 1st -- /bin/sh -c "kill 1"

# Kết quả thì thấy Pod `mc1` được restart và tạo lại container `1st`.
nbt@nbt:~$ kubectl get po -w
NAME    READY   STATUS     RESTARTS        AGE
nginx   2/2     Running    14 (3h6m ago)   4d22h
mc1     1/2     NotReady   12 (16m ago)    3d23h
mc1     2/2     Running    13 (4s ago)     3d23h

# Thực hiện kill process của containers `1st` và `2nd`.
kubectl  exec -it po/mc1 -c 1st -- /bin/sh -c "kill 1"
kubectl  exec -it po/mc1 -c 2nd -- /bin/sh -c "kill 1"

# Kết quả thì thấy Pod `mc1` được restart và tạo lại container `1st` và `2nd`.
nbt@nbt:~$ kubectl get po -w
NAME    READY   STATUS    RESTARTS        AGE
nginx   2/2     Running   14 (3h8m ago)   4d22h
mc1     2/2     Running   14 (17s ago)    3d23h
```
Qua phần demo trên thì thấy khi các containers trong Pod bị kill thì Kubernetes sẽ tự động restart containers mà mình chẳng cần phải làm gì đặc biệt với cái Pod.

*Câu hỏi ở đây là: Làm sao để Kubernetes có thể check được trạng thái có healthy hay không của application từ bên ngoài?.*

## **2.1 Introducing liveness probes**

Kubernetes có thể check nếu containers là vẫn còn alive thông qua `liveness probes`. Mình có thể  mô tả `liveness probes` cho mỗi containers trong phần mô tả Pod. Kubernetes sẽ gọi định kỳ đến `liveness probes` và thực hiện restart nếu containers trả về kết quả là fails.

Kubernetes can probe a container using one of the three mechanisms:

* Có thể sử dụng một HTTP GET request (nếu là spring boot thì có actuator, hoặc có thể viết một API). Nếu mà Kubernetes gọi vào API mà trả về response là 2xx hoặc 3xx thì được coi là healthy, còn ngược lại thì được coi là không healthy và Kubernetes sẽ thực hiện restart.
* An Exec probe executes an arbitrary command inside the container and checks the command’s exit status code. If the status code is 0, the probe is successful. All other codes are considered failures.
* A TCP Socket probe tries to open a TCP connection to the specified port of the container. If the connection is established successfully, the probe is successful. Otherwise, the container is restarted.

## **2.2 Creating an HTTP-based liveness probe** 

**1. API healcheck**

 Cùng nhìn cách để thêm `liveness prove` với spring boot application. Đối với `Spring boot` thì có endpoint để check trạng thái của service `/actuator/health`, hoặc là custom một API để  `liveness prove` thực hiện gọi vào service, nếu API trả về 2xx, 3xx thì được coi là service healthy, còn API trả về 5xx thì service unhealthy.

![](images/1.png)

**2. Define liveness prove**

- **Trường hợp API trả về thành công (2xx, 3xx) khi thực hiện `liveness probe`** 
    ```yaml
    apiVersion: v1
    kind: Pod
    metadata:
      name: demo-liveness-probe
    spec:
      containers:
      - name: demo-liveness-probe
        image: thanhnb1/demo:latest
        resources:
          requests:
            memory: "64Mi"
            cpu: "250m"
          limits:
            memory: "128Mi"
            cpu: "500m"
        ports:
        - containerPort: 8080
        livenessProbe:
          httpGet:
            path: /healCheck/getStatus1
            port: 8080
    ```
    Pod manifest trên có định nghĩa `livenessProbe`, define `httpGet`, muốn nói với Kubernetes là hãy thực hiện gọi API với path là: `/healCheck/getStatus1`, port: `8080` một cách định kỳ, để đảm bảo rằng container vẫn còn running. Kubernetes sẽ thực hiện `livenessProbe` ngay sau khi container run.

    ```
    kubectl apply -f .

    kubectl get po -w
    NAME                  READY   STATUS              RESTARTS        AGE
    demo-liveness-probe   0/1     ContainerCreating   0               4s
    demo-liveness-probe   1/1     Running             0               7s
    ```
    Logs container:
    ```
    2022-07-03 16:09:27.446  INFO 1 --- [           main] com.thanhnb.study.demo.DemoApplication   : Started DemoApplication in 14.904 seconds (JVM running for 17.921)
    2022-07-03 16:09:32.263  INFO 1 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
    2022-07-03 16:09:32.263  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
    2022-07-03 16:09:32.264  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
    /healCheck 1656864572457
    /healCheck 1656864582217
    /healCheck 1656864592216
    /healCheck 1656864602217
    /healCheck 1656864612217
    /healCheck 1656864622218
    /healCheck 1656864632213
    /healCheck 1656864642217
    ```
    Khi thực hiện describe po thì thấy:
    ```
    Liveness:     http-get http://:8080/healCheck/getStatus1 delay=0s timeout=1s period=10s #success=1 #failure=3

    - http-get http://:8080/healCheck/getStatus1  : API mà Liveness thực hiện gọi vào.
    - period=10s                                  : Thực hiện định kỳ sau mỗi 10s. 
    - delay=0s                                    : Liveness thực hiện ngay sau khi container started.
    - timeout=1s                                  : Thời gian timeout của API.
    - failure=3                                   : Container sẽ thực hiện restart nếu liveness faild 3 lần.
    - success=1                                   : Container được coi là healthy nếu liveness thành công 1 lần.
    ```
- **Trường hợp API trả về lỗi (4xx, 5xx) khi thực hiện `liveness probe`** 
    ```yaml
    apiVersion: v1
    kind: Pod
    metadata:
      name: demo-liveness-probe
    spec:
      containers:
      - name: demo-liveness-probe
        image: thanhnb1/demo:latest
        resources:
          requests:
            memory: "64Mi"
            cpu: "250m"
          limits:
            memory: "128Mi"
            cpu: "500m"
        ports:
        - containerPort: 8080
        livenessProbe:
          # httpGet:
          #   path: /healCheck/getStatus1
          #   port: 8080

          httpGet:
            path: /healCheck/getStatus2 # API này sẽ throw Exception và trả về responseCode = 500.
            port: 8080
    ```
    Thực hiện apply và get pods
    ```
    kubectl apply -f pod-liveness-probe.yaml
    pod/demo-liveness-probe created

    kubectl get po
    NAME                  READY   STATUS    RESTARTS        AGE
    demo-liveness-probe   1/1     Running   4 (9s ago)      2m10s
    ```
    Ở trên thì thấy container bên trong Pod `demo-liveness-probe` đang phải restart 4 lần.
    ```
    Events:
    Type     Reason     Age                From               Message
    ----     ------     ----               ----               -------
    Normal   Scheduled  72s                default-scheduler  Successfully assigned default/demo-liveness-probe to nbt
    Normal   Pulled     69s                kubelet            Successfully pulled image "thanhnb1/demo:latest" in 2.225354015s
    Normal   Pulled     39s                kubelet            Successfully pulled image "thanhnb1/demo:latest" in 2.083588449s
    Warning  Unhealthy  11s (x2 over 41s)  kubelet            Liveness probe failed: HTTP probe failed with statuscode: 500
    Normal   Killing    11s (x2 over 41s)  kubelet            Container demo-liveness-probe failed liveness probe, will be restarted
    ```
    Khi thực hiện describe pod thì thấy đang thực hiện liveness faild do API trả về responseCode=500. Liveness đã thực hiện 3 lần nhưng đều trả về 500, nên là Kubernetes thực hiện kill container và restart lại container. Khi mà container bị xóa đi và tạo lại một container mới, nhưng mà vẫn sẽ liveness faild nên vẫn sẽ thực hiện xóa container đi và tạo lại.
    
- **Overide default livenessProbe value**
    ```yaml
    livenessProbe:
    httpGet:
      path: /healthz
      port: 8080
      httpHeaders:
      - name: Custom-Header
        value: Awesome
    initialDelaySeconds: 3 # thực hiện liveness sau khi container started 3s.
    periodSeconds: 3 # thực liện liveness sau mỗi 3s
    ```
**3. Một số các lưu ý để khi thực hiện livenessProbe**

Nên luôn luôn define livenessProbe vì Kubernetes sẽ không có cách nào để biết được application của mình có đang running hay không?. Có livenessProbe thì Kubernetes sẽ biết được application của mình có đang thực sự running hay không thông qua các lần gọi API định kỳ.

- *WHAT A LIVENESS PROBE SHOULD CHECK*
    
    `LivenessProbe` thì là check trạng thái của service đã sẵn sàng để nhận request từ client hay chưa?. Nếu mà không có `livenessProbe` thì service có thể trạng thái là running nhưng thực chất là chưa thể nhận request từ client. VD: Spring boot application thì cần có thời gian để service up (khi service started), nếu khi service chưa chạy xong (service vẫn chưa started, vẫn đang logs, kết nối, ...). Nếu mà lúc này không có `livenessProbe` thì client gọi đến mà trong khi service chưa started => request bị fail.
    
- *KEEPING PROBES LIGHT*

    `LivenessProbe` API thì nên đơn giản thôi, chỉ cần trả về responseCode=200 là được. Nếu mà `LivenessProbe` API trả về response lâu quá thì cũng ảnh hưởng đến việc Kubernetes mất nhiều thời gian đển biết service có healthy hay không? để kill container và re-start container.
