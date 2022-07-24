# #Volumes: attaching disk storage to containers
Ở những phần trước thì đã tìm hiểu về Pod, ReplicationController, ReplicaSet, có vấn đề là khi Pod vì một lý do nào đó mà bị xóa thì các containers bên trong Pod cũng bị xóa => dữ liệu sẽ bị mất. Trong một vài trường hợp thì mình muốn rằng dù containers bên trong Pod được tạo mới thì vẫn còn dữ liệu của containers trước đó. Kubernetes mới đẻ ra một object là `volume`.

Volume được định nghĩa như là 1 phần của Pod, có lifecycle cùng với Pod, có nghĩa là volume sẽ được tạo khi Pod started và bị xóa khi Pod deleted. Sau khi container restarted, container mới có thể nhìn thấy data trong `Volume` mà container trước đó đã tạo ra. Nếu một Pod có nhiều containers thì các containers này có thể dùng chung `volume`.

Tìm hiểu thêm về Docker Volume [Tại đây](https://github.com/nbthanh98/study/blob/master/learn-docker/DOCKER-VOLUME.md)

## **1. Introducing volumes**
Volume trong Kubernetes là một component của Pod được defined in pod’s specification. Một volume thì có thể dùng được nhiều containers bên trong 1 Pod, nhưng container nào dùng volume thì cần phải mount, có thể mount volume đến bất kỳ path nào trong container.

## **2. Using volumes to share data between containers**
Đầu tiên tập chung vào việc làm sao để có thể chia sẻ data giữa các containers bên trong Pod.

### **2.1 Using an emptyDir volume**
`emptyDir` là một loại volume. Giống như cái tên của loại volume này thì nó là một folder empty, các containers có thể chia sẻ data giữa các containers bên trong Pod. Bởi vì lifetime của loại `emptyDir` volume bị ràng buộc với Pod nên Pod bị xóa => volume cũng bị xóa theo.

- **USING AN EMPTY D IR VOLUME IN A POD**
    
    