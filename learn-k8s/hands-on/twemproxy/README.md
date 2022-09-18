# Twemproxy on kubernetes
Twemproxy là opensource giúp điều phối cached data giữa các Redis instances. [Github repo](https://github.com/twitter/twemproxy).
### Deploy Twemproxy on kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: twemproxy
spec:
  selector:
    matchLabels:
      app: twemproxy
  template:
    metadata:
      labels:
        app: twemproxy
    spec:
      containers:
      - name: twemproxy
        image: malexer/twemproxy:latest
        env:
          - name: REDIS_SERVERS
            value: redis01:6379:1
        resources:
          limits:
            memory: "128Mi"
            cpu: "500m"
        ports:
        - containerPort: 6380
---
apiVersion: v1
kind: Service
metadata:
  name: twemproxy
spec:
  selector:
    app: twemproxy
  ports:
  - port: 6380
    targetPort: 6380
```
File yaml trên thì đang sửa dụng image `malexer/twemproxy:latest` [link](https://hub.docker.com/r/malexer/twemproxy).
Khi deploy `Twemproxy` thì sẽ cần 1 file tên: "nutcracker.conf" file này sẽ chưa các configs để kết nối đến Redis.
Khi sử dụng image: `malexer/twemproxy` thì họ đã auto gen cho mình file dựa theo các biến môi trường, file nằm ở folder: `/etc` bên trong container. 
Xem mô tả chi tiết: [link](https://hub.docker.com/r/malexer/twemproxy).
```yaml
pool:
    listen: 0.0.0.0:${LISTEN_PORT}
    hash: fnv1a_64
    distribution: ketama
    redis: true
    auto_eject_hosts: ${AUTO_EJECT_HOSTS}
    timeout: ${TIMEOUT}
    server_retry_timeout: ${SERVER_RETRY_TIMEOUT}
    server_failure_limit: ${SERVER_FAILURE_LIMIT}
    server_connections: ${SERVER_CONNECTIONS}
    preconnect: ${PRECONNECT}
    servers:
        <LIST of SERVERS from ${REDIS_SERVERS}>
```
### Deploy Redis on kubernetes.
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis01
spec:
  selector:
    matchLabels:
      app: redis01
  template:
    metadata:
      labels:
        app: redis01
    spec:
      containers:
      - name: redis01
        image: bitnami/redis:3.2.8-r2
        resources:
          limits:
            memory: "128Mi"
            cpu: "500m"
        ports:
        - containerPort: 6379
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis02
spec:
  selector:
    matchLabels:
      app: redis02
  template:
    metadata:
      labels:
        app: redis02
    spec:
      containers:
      - name: redis02
        image: bitnami/redis:3.2.8-r2
        resources:
          limits:
            memory: "128Mi"
            cpu: "500m"
        ports:
        - containerPort: 6379
---
apiVersion: v1
kind: Service
metadata:
  name: redis01
spec:
  selector:
    app: redis01
  ports:
  - port: 6379
    targetPort: 6379
---
apiVersion: v1
kind: Service
metadata:
  name: redis02
spec:
  selector:
    app: redis02
  ports:
  - port: 6379
    targetPort: 6379
```
### Demo
- Deploy `twemproxy` và `redis`:
  ```shell
  # Deploy twemproxy:
  kubectl apply -f twemproxy/twemproxy-deployment.yaml
  
  # Deploy redis:
  kubectl apply -f twemproxy/redis-deployment.yaml
  
  kubectl get all
  NAME                             READY   STATUS      RESTARTS   AGE
  pod/redis02-6b4cf9ff8-wzsx5      1/1     Running     0          60m
  pod/redis01-546c975788-lnkj2     1/1     Running     0          60m
  pod/twemproxy-56cb5bbd57-qnbcp   1/1     Running     0          51m
  pod/ubuntu                       0/1     Completed   0          50m
  
  NAME                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
  service/redis01     ClusterIP   10.152.183.12    <none>        6379/TCP   60m
  service/redis02     ClusterIP   10.152.183.55    <none>        6379/TCP   60m
  service/twemproxy   ClusterIP   10.152.183.215   <none>        6380/TCP   57m
  ```
- Khi deploy twemproxy xong thì thấy bên trong file "nutcracker.conf" đc tạo ra bên trong folder "etc".
  ```shell
  /etc # cat nutcracker.conf
  pool:
    listen: 0.0.0.0:6380
    hash: fnv1a_64
    distribution: ketama
    redis: true
    auto_eject_hosts: true
    timeout: 2000
    server_retry_timeout: 5000
    server_failure_limit: 1
    server_connections: 40
    preconnect: true
    servers:
     - redis01:6379:1
  ```
- Run pod ubuntu và cài redis-cli để test kết nối đến twemproxy:
  ```shell
  kubectl run -i --tty ubuntu --image=ubuntu --restart=Never /bin/bash
  apt-get update && apt-get install -y redis-tools
  
  redis-cli -h twemproxy -p 6380
  twemproxy:6380> set thanhnb deptrai
  OK
  twemproxy:6380> get thanhnb
  "deptrai"
  twemproxy:6380>
  ```
### Nguồn tham khảo:

- https://github.com/tuananh/kubernetes-twemproxy
- https://dev.to/jakewitcher/creating-a-docker-image-for-a-twemproxy-server-3kop
- https://hub.docker.com/r/malexer/twemproxy