package com.vulnchain.lab.controller;

import com.vulnchain.lab.model.FileUpload;
import com.vulnchain.lab.service.impl.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile (
            @RequestParam("file") MultipartFile file, Authentication auth
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = auth.getName();
            FileUpload saved = fileUploadService.uploadFile(file, username);

            response.put("success", true);
            response.put("message", "File uploaded successfully");
            response.put("filename", saved.getOriginName());
            response.put("contentType", saved.getContentType());
            response.put("path", "/uploads/" + saved.getOriginName());

            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(403).body(response);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", "Upload failed: " + ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<FileUpload>> getMyFiles ( Authentication auth) {
        return ResponseEntity.ok(
                fileUploadService.getUploadsByUser(auth.getName())
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileUpload>> getAllFiles () {
        return ResponseEntity.ok(fileUploadService.getAllUploads());
    }
}
