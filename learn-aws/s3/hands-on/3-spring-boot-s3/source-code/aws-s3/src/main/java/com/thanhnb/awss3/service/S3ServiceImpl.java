package com.thanhnb.awss3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnb.awss3.dto.request.FileUploadRequest;
import com.thanhnb.awss3.dto.request.PreviewImageRequest;
import com.thanhnb.awss3.dto.response.FileUploadResponse;
import com.thanhnb.awss3.dto.response.PreviewImageResponse;
import org.springframework.stereotype.Service;

@Service
public class S3ServiceImpl implements IS3Service {

    private final String AVATAR_FOLDER = "avatar";
    private final String BUCKET_NAME = "thanhnb-demo-test-s3";
    private final AmazonS3 amazonS3;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void createBucket(String bucketName) {
        amazonS3.createBucket(bucketName);
    }

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
}
