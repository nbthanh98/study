## #k8s enough to use

### 1: k8s-overview

* Khi hệ thống của chúng ta có nhiều các service => nhiều container, các containers này có thể được deploy trên nhiều node, vậy thì làm sao để các container này có thể nói chuyện được với nhau, tương tác với nhau. Thì lúc này các container cần có một người "đội trưởng", có nhiệm vụ điều phối, quản lý các containers này hoạt động hiệu quả nhất. Người đội trưởng ở đây là các orchestrator có thể kể đến như: Docker Swarm, Kubernetes,..
* ***Kuberntes có thể làm được gì?***
  * Deploy applications.
  * Scale up or down application.
  * Self-heal it when things break.
  * Perform zero-downtime rolling update and rollbacks.
  * .....
* ***Kuberntes và Docker***
  * Trong mô hình này thì bạn sẽ viết code service (java, php,...), sau đó bạn dùng Docker để đóng gói service, test, và deploy service, cái bước cuối cùng, Deploy và running service thì sẽ do Kuberntes xử lý.
  * Ví dụ bạn đang có khoản 10 Node để chạy các services, trong mỗi node thì sẽ cài *container runtime* có thể là Docker, Containerd,... Điều này có nghĩa là Docker hay các container runtime khác sẽ đứng ở dưới sẽ start, stop các container application. Kuberntes sẽ đứng ở trên nhìn toàn cảnh các node, các service, Kubernetes sẽ là người ra các quyết định như: container sẽ được chạy trên Node nào, quyết định khi nào thì cần scale up hoặc là scale down,...
    ![Screenshot](images/2022-04-11_17-10.png)

### 2: Kubernetes core-concept

Trong phần này, thì chúng ta sẽ nói đến những components quan trọng tạo lên K8s cluster và deploy applications. Mục tiêu của phần này là giới thiệu về các components concept quan trọng trong K8s.

* Master and Nodes.
* Packaging app.
* Declarative configuration and desired state.
* Pods.
* Deployments.
* Services.


