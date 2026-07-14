package com.vulnchain.lab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/v1/avatar")
public class AvatarController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<byte[]> getAvatar(@RequestParam String file) {
        try {
            if (file.contains("../") || file.startsWith("/")) {
                return ResponseEntity.badRequest().build();
            }

            String decodedFile = java.net.URLDecoder.decode(file, java.nio.charset.StandardCharsets.UTF_8);
            java.io.File targetFile = new java.io.File(uploadDir + "/" + decodedFile);
            if (!targetFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] content = Files.readAllBytes(targetFile.toPath());
            String contentType = "application/octet-stream";
            if (file.endsWith(".jpg") || file.endsWith(".jpeg"))
                contentType = "image/jpeg";
            else if (file.endsWith(".png"))
                contentType = "image/png";
            else if (file.endsWith(".gif"))
                contentType = "image/gif";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(415)
                    .body(java.util.Map.of("success", false, "message", "Only image files allowed"));
        }

        try {
            String username = auth.getName();
            String filename = "avatar_" + username + ".jpg";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(java.util.Map.of(
                    "success", true,
                    "filename", filename
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("success", false, "message", e.getMessage()));
        }
    }
}