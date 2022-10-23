# AWS Identity and Access Management (IAM)

![](./images/1.png)

- [AWS Identity and Access Management (IAM)](#aws-identity-and-access-management-iam)
  - [**1. Introduction**](#1-introduction)
  - [**2. Defining permissions with an IAM identity policy**](#2-defining-permissions-with-an-iam-identity-policy)
  - [**3. Hands-on**](#3-hands-on)
    - [**3.1. Tạo một tài khoản mới (IAM user) và define `Policy` phân quyền truy cập đến S3 Bucket.**](#31-tạo-một-tài-khoản-mới-iam-user-và-define-policy-phân-quyền-truy-cập-đến-s3-bucket)

## **1. Introduction**

Là một service giúp quản lý các quyền truy cập đến các resource của trên AWS. Resource ở đây là các users, các service của AWS (S3, lamda,...). Khi mà nghĩ đến IAM thì sẽ nghĩ đến `identities` and `permissions`:

- `Identities`: Khi có một hành động nào đó tương tác với các resource trên AWS thì cần biết cái request đó là của ai (Authencation) có thể là của user, hoặc là của các entity khác. VD: Ông A đang muốn tạo một cái S3 Bucket.
- `Permissions`: Khi mà biết ông A là người request tạo S3 Bucket rồi thì cần phải biết cái request này của ông A có quyền thực hiện action đó hay không?. Thì các action mà ông A có thể làm sẽ được định nghĩa trong Policy.
- `IAM user`: is used to authenticate people or workloads running outside of AWS.
- `IAM group`: is a collection of IAM users with the same permissions.
- `IAM role`: is used to authenticate AWS resources.
- `IAM identity policy`: is used to define the permissions for a user, group, or role.

## **2. Defining permissions with an IAM identity policy**

Để cấp quyền cho IAM User hoặc IAM role quản lý các resources trên AWS thì cần define các `IAM identity policies` rồi gán cho IAM user hoặc IAM role. `Identity policies` được define trong JSON và chứa một hoặc nhiều `statement`, các statement có thể là `allow or deny` cho một hoặc nhiều action trên một hoặc nhiều resources AWS.

```json
{
  "Version": "2012-10-17", // Version này là fix, như apiVersion trong K8s vậy.
  "Statement": [
    // Danh sách các statement
    {
      "Effect": "Allow", // Effect: (Allow/Deny), ở đây là Allow -> cho phép thực hiện các action ở field "action".
      "Action": "ec2:*", // thực hiện tất cả các action trên EC2. vì có "*".
      "Resource": "*" // Trên tất cả các resource.
    }
  ]
}
```

Có thể định nghĩa cả `Effect` là `Allow` hoặc `Deny`. Ở đây là cho phép thực hiện tất cả các actions trên EC2 trừ action `TerminateInstances`.

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "ec2:*",
      "Resource": "*"
    },
    {
      "Effect": "Deny",
      "Action": "ec2:TerminateInstances",
      "Resource": "*"
    }
  ]
}
```

Ở 2 Policy json trên đều để `"Resource": "*"` tác động đến tất cả các resource, giờ muốn define chỉ tác động đến một số các resources cụ thể thôi thì sẽ dùng Amazon Resource Name (ARN):

![](./images/2.png)

Lấy thông tin ARN này ở phần properties của service. Lúc đó thì cái `Policy` json sẽ như sau:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "ec2:TerminateInstances",
      "Resource": [
        "arn:aws:ec2:us-east-1:111111111111:instance/i-0b5c991e026104db9"
      ]
    }
  ]
}
```

## **3. Hands-on**

### **3.1. Tạo một tài khoản mới (IAM user) và define `Policy` phân quyền truy cập đến S3 Bucket.**

- Vào web consolve -> chọn dịch vụ IAM -> tạo tài khoản.

  ![](./images/3.png)

- Tạo Policy hoặc có thể dùng các Policy có sẵn.

  - Vào [awspolicygen](https://awspolicygen.s3.amazonaws.com/policygen.html) để thực hiện gen Policy.
  - Chọn service -> chọn Effect(Allow/Deny) -> chọn Action -> Điền ARN -> Gen Policy.

    ```json
    {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Sid": "Stmt1666516164288",
          "Action": "s3:*",
          "Effect": "Allow",
          "Resource": [
            "arn:aws:s3:::thanhnb-test-3",
            "arn:aws:s3:::thanhnb-test-3/*"
          ]
        }
      ]
    }
    ```

  - Điền cái Policy mới gen đc ở trên vào phần Policy trên AWS.

    ![](./images/4.png)

  - Đặt tên và description cho policy => Tạo Policy.

    ![](./images/5.png)

- Gán Policy cho User -> chọn "Attach existing policies directly" -> search "s3-policy-test" mới tạo ở bước trên.

  ![](images/6.png)

- Thực hiện tạo user là xong.
- Đăng nhập thử vào user "quang" mới tạo và access thử vào S3 bucket "thanhnb-test-3" xem có access đc hay không?

  User "quang" đã có thể truy cập đc đến S3 bucket "thanhnb-test-3".

  ![](images/7.png)

  User "quang" KHÔNG truy cập đc đến S3 bucket "thanhnb-demo-s3" vì user "quang" đang không có Policy nào gắn với resource S3 bucket "thanhnb-demo-s3".

  ![](images/8.png)
