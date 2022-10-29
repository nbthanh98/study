package com.thanhnb.awss3.controllers;

import com.thanhnb.awss3.dto.request.FileUploadRequest;
import com.thanhnb.awss3.dto.request.PreviewImageRequest;
import com.thanhnb.awss3.dto.response.FileUploadResponse;
import com.thanhnb.awss3.dto.response.PreviewImageResponse;
import com.thanhnb.awss3.service.IS3Service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/s3")
public class S3Controller {

    private final IS3Service is3Service;

    public S3Controller(IS3Service is3Service) {
        this.is3Service = is3Service;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<FileUploadResponse> upload(@RequestParam(name = "bucketName") String bucketName,
                                                     @RequestParam(name = "file") MultipartFile multipartFile) {
        return ResponseEntity.ok(is3Service.uploadFile(new FileUploadRequest(bucketName, multipartFile)));
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> previewImage(@RequestParam(value = "filePath") String filePath) {
        PreviewImageResponse previewImageResponse = is3Service.previewImage(PreviewImageRequest.make(filePath));
        return ResponseEntity.ok()
                .contentLength(previewImageResponse.getBytesData().length)
                .header("Content-type", "image/jpeg")
                .header("Content-disposition", "attachment; filename=\"" + previewImageResponse.getFileName() + "\"")
                .body(new ByteArrayResource(previewImageResponse.getBytesData()));
    }
}
