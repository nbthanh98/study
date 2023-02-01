# AWS Identity and Access Management (IAM)

![](./images/1.png)

- [AWS Identity and Access Management (IAM)](#aws-identity-and-access-management-iam)
  - [**1. Introduction**](#1-introduction)
  - [**2. What are IAM User?**](#2-what-are-iam-user)
  - [**3. What is IAM Group?**](#3-what-is-iam-group)
  - [**4. IAM Policy and Permissions**](#4-iam-policy-and-permissions)

## **1. Introduction**

AWS IAM (Identity and Access Management) cho phép kiểm soát việc tương tác với các resource trên AWS. Có hai keywork ở đây là "Who" và "Permissions". "Who" refer to specific identity, which can be a **user**, **user group** or **role**. "Permissions" refer to the **policies** that attach to an identity. "Permissions" description **allow** or **deny** identity access to resource on AWS.

## **2. What are IAM User?**

Identity ở đây có thể là Developer, SysAdmin,... gọi chung là người (human) hoặc cũng có thể là các Applications mà cần tương tác với các resource trên AWS. If Identity is human can access to AWS resource by login to AWS Console with user/password. If Identity are applications can access to AWS resource with access keys.

## **3. What is IAM Group?**

IAM User can placed in IAM Group. IAM Group make it easier to organies a large number IAM Users. IAM Group can attach permisstion to group level instead attach permissions to each IAM User.

Each IAM User can placed in multiple IAM Groups. Example a user can placed in DevOps Group and Developer Group. 

## **4. IAM Policy and Permissions**

IAM Policies is Json document file let you define permisstions such as:

- Who can access to AWS resources?.
- Which AWS resoueces are allowed to access?
- What actions allowed or deny?
- When AWS resources can be acceed?