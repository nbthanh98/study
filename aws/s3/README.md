# Simple storage service - S3

## What is AWS S3?

AWS S3 là một file storage service, S3 đảm bảo cho về scalability, data availability, security and performance. Mình sẽ tương tác với S3 thông qua internet. S3 có thể sử dụng cho những mục đích như:

- Backups data.
- Storage
- Hosting static websites.

## S3 bucket

Một `S3 bucket` giống như là một directory. `S3 bucket` có thể được định nghĩa một số các quyền, storage rules cho tất của các files mà `S3 bucket` chứa. S3 là một global service (vì cái bucketName sẽ là unique trên toàn cầu, vd: đặt một cái bucketName= "helloworld" thì sẽ không có ai dùng lại được cái bucketName="helloworld" cho đến khi bucket bị xóa đi.) nhưng `S3 bucket` lại được defined ở region level (bên trong region là các AZ (Availability Zones) có nghĩa là các data được upload lên S3 sẽ được xao lưu ở các AZ bên trong Region).
