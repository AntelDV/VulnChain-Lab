package com.vulnchain.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private Long id;
    private String originName;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String uploaderUsername;
}
