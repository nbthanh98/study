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