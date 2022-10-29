package com.thanhnb.awss3.service;

import com.thanhnb.awss3.dto.request.FileUploadRequest;
import com.thanhnb.awss3.dto.request.PreviewImageRequest;
import com.thanhnb.awss3.dto.response.FileUploadResponse;
import com.thanhnb.awss3.dto.response.PreviewImageResponse;

public interface IS3Service {
    void createBucket(String bucketName);

    FileUploadResponse uploadFile(FileUploadRequest request);

    PreviewImageResponse previewImage(PreviewImageRequest request);
}
