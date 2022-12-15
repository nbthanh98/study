# DNS 101


- [DNS 101](#dns-101)
  - [What is does?](#what-is-does)
  - [How DNS actually work?](#how-dns-actually-work)
    - [How query work within DNS](#how-query-work-within-dns)



## What is does?

![](images/1-dns.png)

When you access to any website, example: `www.netflix.com` you might imagine that name is uesed to connect to the `netflix.com` server and stream your movie or TV show, but that's not actually how it work or any internet app works. To connect to `netflix` server, your computer and any networking in between needs the IP address of the `Netflix` servers. DNS server links names to IP address. 

**1. DNS (Domain Name System)**

The DNS (Domain name system), more commonly known as `DNS` is netwoking system that allow you resole human-friendly name to unique IP address.

**2. Domain Name**

Domain name is the human-friendly name that we used to access internet resource. For instance, `www.netflix.com` is domain name. The URL `www.netflix.com` is access with server owned by `Netflix`. The Domain name system allow you access `Netflix` servers with domain name.

**3. IP Address**

IP address is what we call a network addressable localtion. Each IP address must be unique within its network. When we are taking ablout websites. this is the entire internet. With DNS , we maps a name to that address so that you do not remember IP addess for each you access to servers.

![](images/3-dns.png)

When using DNS when accessing `Netflix`, we ask DNS for the IP Address of `netflix` and DNS Server return IP address. Your computer will use IP address (return from DNS) to access `Netflix` server. DNS change `netflix.com` to IP `44.240.158.19`.

## How DNS actually work?

![](./images/4-dns.png)

When you access to website, example `www.netflix.com` you need IP address which provides services for that name, `DNS Zone` which link `www.netflix.com` to one or more IP address. The issies is how do we find this zone?

### How query work within DNS

