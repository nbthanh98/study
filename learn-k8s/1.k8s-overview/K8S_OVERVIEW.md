    

## Kubernetes overview

![Screenshot](images/kubernetes-watt.jpg?raw=true "Optional Title")

### 1. Kubernetes background

Phần lớn các dự án hiện nay điều được chia nhỏ thành nhiều các microservice (mỗi service thực hiện nghiệp vụ riêng). Khi thực hiện deploy một service thì cách phổ biến hiện nay là container hóa các service, mỗi service sẽ một hoặc nhiều container tùy thuộc vào service đó muốn chạy bao nhiêu instance. Như vậy thì một dự án sẽ có nhiều các service và mỗi service lại có thể có một hoặc nhiều container, lúc này thì sẽ đẻ ra rất nhiều containers, và giờ mình phải có một công cụ gì đó để quản lý giúp chúng ta các containers này và đảm bảo được các service hoạt động tốt với nhau, vì vậy `container orchestration` được nói đến (Docker swarm hoặc kubernetes) sẽ giúp chúng ta quản lý các containers.

### 2. What Kubernetes can do?

Kubernetes có thể giúp mình deploy và quản lý các application. Nó có thể deploy application và có thể  tự động thực hiện một số các hành động để đảm bảo application high availability. VD: kubernetes có thể làm những việc sau:

* Deploy applications.
* Scale application up or down tự động theo yêu cầu.
* Heal check.
* Xử lý zero-downtime mỗi khi deploy một version mới của application.
* .......
