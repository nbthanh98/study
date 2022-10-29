package com.thanhnb.awss3.dto.response;

import lombok.Data;

@Data
public class PreviewImageResponse {
    private String fileName;
    private byte[] bytesData;
}
