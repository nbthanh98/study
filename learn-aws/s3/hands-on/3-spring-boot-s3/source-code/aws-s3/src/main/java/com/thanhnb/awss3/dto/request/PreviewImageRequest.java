package com.thanhnb.awss3.dto.request;

import lombok.Data;

@Data
public class PreviewImageRequest {
    private String bucketName;
    private String fileName;
    private String filePath;

    public static PreviewImageRequest make(String filePath) {
        PreviewImageRequest request = new PreviewImageRequest();
        String[] strings = filePath.split("/");
        request.setFileName(strings[1]);
        request.setFilePath(filePath);
        return request;
    }
}
