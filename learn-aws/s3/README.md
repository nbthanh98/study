# AWS S3 (Simple storage service)

![](images/amazon-s3.png)

- [AWS S3 (Simple storage service)](#aws-s3-simple-storage-service)
  - [**1. What is Amazon S3?**](#1-what-is-amazon-s3)
  - [**2. Những ưu điểm của S3**](#2-những-ưu-điểm-của-s3)
    - [**Horizontally Scalable**](#horizontally-scalable)
    - [**Consistently Available**](#consistently-available)
    - [**Durability**](#durability)
    - [**Integrations with Other AWS Services**](#integrations-with-other-aws-services)
  - [**3. S3 Core Concepts**](#3-s3-core-concepts)
    - [**Buckets**](#buckets)
    - [**Objects**](#objects)
  - [**4. Labs**](#4-labs)
  - [**5. Resources**](#5-resources)

## **1. What is Amazon S3?**

S3 stands for **S**imple **S**torage **S**ervice and is **object storage** service built by AWS. I like to think of S3 as something simalar to Dropbox or Google Drive in the sense that that you can store any type of objects to S3. Example you can store video, file,...

You can store big files, small files, media content, source code, email, json and basically anything that you can think of. Keep inx mind though for a single object, there is a maximum size limit of **5 TB**.

## **2. Những ưu điểm của S3**

### **Horizontally Scalable**

**Performance** is another reason many users flock to S3 as an object storage solution. S3 is an extremely scalable solution. S3 no limit to the amount of content you can upload.

The nice thing abloud S3 is that it can support applications that need to PUT or GET objects at very hight throughputs, and still experiendce very very low latancies. For example, can build application that needed to read s3 object out of a bucket at at over 50 read calls **PER SECOND**. Volum size from around 50KB to 100KB, but latencies were always low (often lower than 100ms). 

### **Consistently Available**

// add some contents here.

### **Durability**

// add some contents here.

### **Integrations with Other AWS Services**

Có thể sử dụng S3 kết hợp với các service khác trên AWS. VD có thể sử dụng S3 **event** để tương tác với lambda function. Có thể trigger một lambda function mỗi khi một S3 object được upload.

Cũng có thể tự host một websites (HTML, CSS, Javascript) với Route53. Cũng có thể thêm caching bằng cách sử dụng CloudFront.

## **3. S3 Core Concepts**

### **Buckets**

- Hãy nghỉ một bucket giống như foler trong máy tính, bản thân bucket này là một folder, bên trong bucket này có thể có các items (thường gọi là các **S3 objects**) hoặc có thể là các subfolers.

- Khi tạo một bucket cần đặt tên cho bucket, cái tên của bucket sẽ là duy nhất trên toàn AWS. Bạn không thể tạo hai bucket tên là `test` hoặc `production` mặc dù hai bucket trên được tạo bởi ai đó và trên một tài khoản khác.

### **Objects**

- Objects là content mà lưu trên S3 bucket. Các object này có thể là files, media, zipfile, json,... Một S3 object có size limit là **5 TB**. Object khi thực hiện upload lên S3 bucket không được vượt qua size limit trên.

- Những objects có dung lượng lớn khi thực hiện upload lên S3 thì có thể sẽ gặp một số vấn đề mạng,... vì thực hiện upload objects thông qua internet. Những objects có dung lượng lớn thì có thể chia thành những file nhỏ và sử dụng tính năng multiple upload.

## **4. Labs**

- [Create bucket upload to S3](https://github.com/nbthanh98/study/tree/master/learn-aws/s3/hands-on/1-create-bucket-manager-console#readme)
- [S3 with cli](https://github.com/nbthanh98/study/tree/master/learn-aws/s3/hands-on/2-upload-download-file-with-cli)
- [Upload and download file spring boot and S3](https://github.com/nbthanh98/study/tree/master/learn-aws/s3/hands-on/3-spring-boot-s3)

## **5. Resources**

- https://zacks.one/aws-s3-lab/

- https://medium.com/@venkatesh111/aws-s3-security-iam-policies-bucket-polices-acl-53aa73f7954a
