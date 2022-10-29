package com.thanhnb.awss3.dto.response;

import lombok.Data;

@Data
public class FileUploadResponse {
    private String fileName;
    private String url;
}
