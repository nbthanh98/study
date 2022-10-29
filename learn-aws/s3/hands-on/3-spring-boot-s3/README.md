# Upload and download file with Sping boot and AWS S3

**Link source code**: [spring-boot-s3](https://github.com/nbthanh98/study/tree/master/learn-aws/s3/hands-on/3-spring-boot-s3/source-code/aws-s3).

Phần này test upload và download file với S3 và Spring boot. Cần chuẩn bị:

- Spring project (maven/gradle) -> tạo [spring project](https://start.spring.io/).
- Import SDK S3 maven hoặc gradle

  ```xml
  <dependency>
    <groupId>com.amazonaws</groupId>
  	<artifactId>aws-java-sdk</artifactId>
  	<version>1.11.163</version>
  </dependency>
  ```

- Tạo trước 1 bucket.
- Tạo IAM User, set Policy với S3 Bucket đã tạo trước đó.

  - Vào web console -> IAM -> tạo IAM User `thanhnb-demo-test-s3`.

    ![](../../images/13.png)

  - Tạo policy `s3-policy-service` và gán cho IAM User `thanhnb-demo-test-s3` tạo trước đó.

    ```json
    {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Sid": "Stmt1667061348600",
          "Action": ["s3:CreateBucket", "s3:GetObject", "s3:PutObject"],
          "Effect": "Allow",
          "Resource": [
            "arn:aws:s3:::thanhnb-demo-test-s3",
            "arn:aws:s3:::thanhnb-demo-test-s3/*"
          ]
        }
      ]
    }
    ```

- Sau khi tạo đc IAM User và gán Policy cho IAM User thì lấy `access key` và `private access key` để cấu hình

  ```java
  @Configuration
  public class AWSConfigs {

    public AWSCredentials credentials() {
        // chỗ key này nên để thành biến môi trường
        return new BasicAWSCredentials(
                "AKIA2KJPHCMT3JLW4EGF",
                "SPlvaksdi25MTgP7+RwCQ5d7RwCZig0ykgHo14He"
        );
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.US_EAST_1)
                .build();
    }
  }
  ```

## **1. Upload file**

- Controller
  ```java
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<FileUploadResponse> upload(@RequestParam(name = "bucketName") String bucketName,
                                                    @RequestParam(name = "file") MultipartFile multipartFile) {
       return ResponseEntity.ok(is3Service.uploadFile(new FileUploadRequest(bucketName, multipartFile)));
    }
  ```
- Service

  ```java
    @Override
    public FileUploadResponse uploadFile(FileUploadRequest request) {
        FileUploadResponse response = new FileUploadResponse();

        if (request.getMultipartFile().isEmpty()) throw new IllegalStateException("file cannot empty");

        boolean isExistBucket = amazonS3.doesBucketExist(request.getBucketName());
        if (!isExistBucket) this.createBucket(request.getBucketName());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.addUserMetadata("Content-Type", request.getMultipartFile().getContentType());
        objectMetadata.addUserMetadata("Content-Length", String.valueOf(request.getMultipartFile().getSize()));

        String path = String.format("%s/%s", BUCKET_NAME, AVATAR_FOLDER);
        String fileName = String.format("%s", System.currentTimeMillis() + request.getMultipartFile().getOriginalFilename());
        try {
            amazonS3.putObject(path, fileName, request.getMultipartFile().getInputStream(), objectMetadata);
            response.setUrl(String.format("%s/%s", AVATAR_FOLDER, fileName));
            response.setFileName(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
  ```

- Test:

  - Dung postman call api upload

    ![](./../../images/14.png)

  - Lên web console xem thì đã thấy file được upload lên S3 bucket.

    ![](./../../images/15.png)

## **2. Download file**

- Controller
  ```java
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> previewImage(@RequestParam(value = "filePath") String filePath) {
        PreviewImageResponse previewImageResponse = is3Service.previewImage(PreviewImageRequest.make(filePath));
        return ResponseEntity.ok()
                .contentLength(previewImageResponse.getBytesData().length)
                .header("Content-type", "image/jpeg")
                .header("Content-disposition", "attachment; filename=\"" + previewImageResponse.getFileName() + "\"")
                .body(new ByteArrayResource(previewImageResponse.getBytesData()));
    }
  ```
- Service. Dùng amazonS3.getObject(...) để download file từ S3.
  ```java
  @Override
  public PreviewImageResponse previewImage(PreviewImageRequest request) {
      PreviewImageResponse response = new PreviewImageResponse();
      try {
          S3Object s3Object = amazonS3.getObject(new GetObjectRequest(BUCKET_NAME, request.getFilePath()));
          response.setBytesData(s3Object.getObjectContent().readAllBytes());
          response.setFileName(request.getFileName());
      } catch (Exception e) {
          e.printStackTrace();
          return null;
      }
      return response;
  }
  ```
- Test call thử api preview

  ![](../../images/16.png)
