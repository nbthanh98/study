package com.thanhnb.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

@Slf4j
@RestController
@RequestMapping(value = "/s3-assume-role")
public class S3AssumeRoleControllers {

    @GetMapping(value = "/get-objects")
    public String getObjectS3() {
        listObjects("thanhnb-test-policies", buildS3Client(Region.US_EAST_1));
        return "oke";
    }

    @GetMapping(value = "/hello")
    public String sayHello() {
        return "hello";
    }

    private static S3Client buildS3Client(String accessKey, String secretKey, Region region) {
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey, secretKey));
        return S3Client.builder().
                credentialsProvider(staticCredentialsProvider)
                .region(region).build();
    }

    private static S3Client buildS3Client(Region region) {
        return S3Client.builder().
                credentialsProvider(InstanceProfileCredentialsProvider.create())
                .region(region).build();
    }

    public void listObjects(String bucketName, S3Client s3Client) {
        ListObjectsV2Iterable listObjectsV2Iterable = s3Client.listObjectsV2Paginator(builder -> builder.bucket(bucketName));
        log.info("Objects in {} bucket: ", bucketName);
        listObjectsV2Iterable.contents().stream().forEach(content -> log.info("{} {} bytes", content.key(), content.size()));
    }
}
