    

## Kubernetes overview

![Screenshot](images/kubernetes-watt.jpg?raw=true "Optional Title")

### 1. Kubernetes background

Phần lớn các dự án hiện nay điều được chia nhỏ thành nhiều các microservice (mỗi service thực hiện nghiệp vụ riêng). Khi thực hiện deploy một service thì cách phổ biến hiện nay là container hóa các service, mỗi service sẽ một hoặc nhiều container tùy thuộc vào service đó muốn chạy bao nhiêu instance. Như vậy thì một dự án sẽ có nhiều các service và mỗi service lại có thể có một hoặc nhiều container, lúc này thì sẽ đẻ ra rất nhiều containers, và giờ mình phải có một công cụ gì đó để quản lý giúp chúng ta các containers này và đảm bảo được các service hoạt động tốt với nhau, vì vậy `container orchestration` được nói đến (Docker swarm hoặc kubernetes) sẽ giúp chúng ta quản lý các containers.

### 2. What Kubernetes can do?

Kubernetes có thể giúp mình deploy và quản lý các application. Nó có thể deploy application và có thể  tự động thực hiện một số các hành động để đảm bảo application high availability. VD: kubernetes có thể làm những việc sau:

* `Service discovery and load balancing`: Kubernetes có thể expose một container sử dụng service name hoặc là IP của container. Request vào containers sẽ được chia đều để tăng khả năng chịu tải của containers.
* `Automated rollouts and rollbacks`: Bạn có thể khai báo trạng thái mong muốn của service, và Kubernetes sẽ tự động thay đổi trạng thái thực của service theo như mong muốn.
* `Self-monitoring`: Kubernetes có thể tự monitor liên tục trạng thái của các service và các Node.
* `Self-healing`: Kubernetes có thể restart các containers nếu chúng fail, có thể  xóa bỏ container thay thế bằng các containers khác, nếu các container này khi K8s thực hiện gọi healcheck vẫn trả kết quả là fail.
* `Horizontal scaling`: Kuberntes có thể giúp mình scale tự động các containers dựa trên một số tiêu trí mà mình định nghĩa.
* `Container balancing`: Khi thực hiện deploy một containers mới thì K8s sẽ tìm kiếm một Node phù hợp nhất để có thể deploy containers này.
* `Run everywhere`: 
* `Secret and configuration management`: Kubernetes lets you store and manage sensitive information.
* .......
