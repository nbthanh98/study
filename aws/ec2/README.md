# Amazon EC2 basic

## 1. Amazon EC2

- EC2 is one of the most popular of AWS' offering.
- EC2 = Elastic Compute Cloud = Infrastructure as a Service.
- Các đặc tính của Amazon EC2:

  1. **Scaling:**

     - Scaling Up/Down: Tăng/Giảm (CPU, RAM,..) của instance.
     - Scaling In/Out: Tăng/giảm số lượng instance.

  2. **Security:**

     - Có thể thiết lập rank IP Private dành cho riêng EC2.
     - Sử dụng Security Group và NACLS để control inbound/outbound.

  3. **Cost:**

     - On-Demand instance: Tính theo giờ, đáp ứng nhu cầu dùng trong thời gian ngắn. Dùng bao nhiêu, trả bấy nhiêu.
     - Reserved instance: Cho phép trả trước 1 lượng server cho 1 hoặc 3 năm. Chi phí chỉ bằng 75% so với On-Demand.

## 2. EC2 sizing & configuration options

- OS: Linux, Windows, Mac OS.
- How much compute power & core (CPU).
- How much random-access memory (RAM).
- How much storage space:

  - Network-attached (EBS & EFS)
  - Hardware (EC2 Instance store)

- Network card: speed of the card, Public IP address.
- Firewall rules: security group

## 3. Hands-on

1. Vào AWS Console -> chọn EC2.
2. Chọn OS (AMI- Amazon Machine Image):

   ![](./images/1.png)

   - Amazon linux 2 AMI nằm trong Free Tier.
   - 64 bit (x86).

3. Chọn EC2 instance type:

   ![](./images/2.png)

   - Chọn loại instance type (t2.micro) để dùng free tier trong năm đầu sử dụng, 1 CPU, 1GiB memory.

4. Networking settings:

   ![](./images/3.png)

5. Storage:

   ![](./images/4.png)

6. Connect EC2 via SSH:

   ```powershell
   # chmod 400 <file.pem>
   chmod 400 EC2-tutorial.pem
   # Connect to your instance using its Public DNS
   ssh -i "EC2-tutorial.pem" ec2-user@ec2-54-196-241-151.compute-1.amazonaws.com
   ```

   ![](images/5.png)
