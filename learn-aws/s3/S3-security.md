## S3 bucket policies

Bucket policies là resource-base policies, giống như indetity-base policies nhưng indetity-based policies sẽ attach vào identity, còn resource-based policies sẽ attach vào resources ở đây là S3.

Resource-based policies có thể  định nghĩa ALLOW/DENY các identity cùng hoặc khác tài khoản. Còn đối với identity-based polices thì chỉ có thể attach vào những identity cùng tài khoản.

Resousece-based policies có thể ALLOW/DENY `anonymous` principals. 