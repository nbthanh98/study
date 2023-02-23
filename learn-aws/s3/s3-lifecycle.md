## S3 Lifecycle

Là một danh sách các rule sẽ được áp dụng cho bucket, những cái rule này sẽ bao gồm các hành động mà sẽ áp dụng dựa trên các tiêu chí (do X if Y true).

Những cái rule này thì có thể áp dụng cho bucket hoặc nhóm các objects.

Các actions sẽ được áp dụng môt trong hay loại sau: 

- `Transition actions`: Thay đổi giữa các loại storage class. VD: có thể thay đổi objects từ S3 Standard vào S3 Standard-IA sau 30 ngày, làm điều này nếu chắc chắn rằng các object này sẽ không được truy cập thường xuyên nữa sau 30 ngày. Cũng có thể chuyển object từ S3 Standard-IA sang S3 Glacier Deep Archive sau 90 ngày khi chắc chắn rằng object này hiếm khi được truy cập.
- `Expiration actions`: Có thể delete một hoặc nhiều object hoặc object version. Bạn muốn rằng object hoặc version object sẽ bị expire sau một thời gian nào đó. Điều này thì có thể sẽ giúp giữ cho bucket clean hơn.

**TÓM CÁI QUẦN**: S3 lifecycle là cách mà sẽ configs để tự động xóa object/object version hoặc là thay đổi storage class của objects để tối ưu hóa chi phí. Các rule này sẽ KHÔNG dựa trên tần suất truy cập object, không thể di chuyển các objects dựa trên tần suất truy cập object. Cái này thì khác so với tính năng của `S3 Intelligent-Tiering` (tự động di chuyển các object dựa trên tần suất truy cập).