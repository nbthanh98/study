# AWS Identity and Access Management (IAM)

![](./images/1.png)

- [AWS Identity and Access Management (IAM)](#aws-identity-and-access-management-iam)
  - [**1. Introduction**](#1-introduction)
  - [**2. What are IAM User?**](#2-what-are-iam-user)
  - [**3. What is IAM Group?**](#3-what-is-iam-group)
  - [**4. IAM Policy and Permissions**](#4-iam-policy-and-permissions)
    - [**4.1 Types of IAM Policies**](#41-types-of-iam-policies)
    - [**4.2 Indentity-based Policies**](#42-indentity-based-policies)
    - [**4.3 Users**](#43-users)

## **1. Introduction**

AWS IAM (Identity and Access Management) cho phép kiểm soát việc tương tác với các resource trên AWS. Có hai keywork ở đây là "Who" và "Permissions". "Who" refer to specific identity, which can be a **user**, **user group** or **role**. "Permissions" refer to the **policies** that attach to an identity. "Permissions" description **allow** or **deny** identity access to resource on AWS.

## **2. What are IAM User?**

Identity ở đây có thể là Developer, SysAdmin,... gọi chung là người (human) hoặc cũng có thể là các Applications mà cần tương tác với các resource trên AWS. If Identity is human can access to AWS resource by login to AWS Console with user/password. If Identity are applications can access to AWS resource with access keys.

## **3. What is IAM Group?**

IAM User can placed in IAM Group. IAM Group make it easier to organies a large number IAM Users. IAM Group can attach permisstion to group level instead attach permissions to each IAM User.

Each IAM User can placed in multiple IAM Groups. Example a user can placed in DevOps Group and Developer Group. 

## **4. IAM Policy and Permissions**

IAM Policies is a set of rules let you define permisstions such as:

- Who can access to AWS resources?.
- Which AWS resoueces are allowed to access?
- What actions allowed or deny?
- When AWS resources can be acceed?

![](./images/identity-base-policies-resource-base-policies.png)

By default, IAM identity (User, user group, role) start with no permisstions. IAM identity need permisstions for access AWS resources. AWS permisstions managed by IAM Policies.

### **4.1 Types of IAM Policies**

- **Identity-based policies**: Identity-based policies are policies attach to AWS identities like IAM User, IAM Group, IAM Roles. For example, you can attach IAM Policies to IAM User called "BOB" to list item in S3 bucket. 

  - **Managed Policies**: Là những policies được gắn với IAM identity như: IAM User, IAM Group, IAM Role, có thể tái sử dụng.
  
    - **AWS Managed**: These policies are create, update, managed by AWS. By default, there are many pre-defined AWS managed policies available in your AWS account.

    - **Customer Managed**: These policies are create, update, manage by Customer. Customer can create their own policies for customized environment. By default, in your aws account dont have any customer managed policies.
  
  - **Inline Policies**: Là những policies nhúng trực tiếp vào một identity nào đó (user, group, role). Quan hệ 1-1 với identity, những policies này thì không tái sử dụng được và sẽ bị xóa đi nếu các identity bị xóa.

- **Resource-based policies**: Resource-based policies là những policies sẽ được gắn với các resource (S3, EC2,...) trên AWS.

### **4.2 Indentity-based Policies**

Identity-based policies are the easier of the two, as some variation of it is present in most system. They **define what an indentity can do**, sush as a user access to an S3 bucket.

There are two types of identites in AWS (**users** and **roles**). By attaching a policies to an identity you can give it permisstions to access resources. For example, you can attach this policy to allow a user read an object in an S3 bucket:

```json
{
	"Version": "2012-10-17",
	"Statement": [
		{
			"Action": [
				"s3:GetObject"
			],
			"Effect": "Allow",
			"Resource": "arn:aws:s3:::<bucket>/text.txt"
		}
	]
}
```
When attach the policy to an identity, a user in this case, there is no "Principal" field in the policy. The above policy defined permisstions to access S3 bucket.

![](images/policy-3.png)

**Note**: Groups are not identities but a way to attach policies to multiple user.

### **4.3 Users**

