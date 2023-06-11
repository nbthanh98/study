## Default VPC

- Virtual Private Cloud (VPC) is the service you will use to create Virtual private network inside AWS.
- VPC is regional service meaning they are regionally resilient. VPCs operate from multiple AZ in a specific AWS region.
- VPC private and isolate. Service deployed into the same VPC can communicate, but the VPC is isolated from other VPCs. 
- Two types: Default VPC and Custom VPCs in side a region. 1 Default VPC in per region and can have many custom VPCs in a region.
- Default VPC CICD is always 172.31.0.0/16. In the region each AZ have /20 subnet.
- Internet Gateway(IGW), Security Group (SG) & NACL
- Subnets assign public IPv4 address.