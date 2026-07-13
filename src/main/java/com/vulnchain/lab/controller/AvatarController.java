package com.vulnchain.lab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;


@RestController
@RequestMapping("/api/v1/avatar")
public class AvatarController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<byte[]> getAvatar(@RequestParam String file) {
        System.out.println("=== RAW file param: " + file);
        try {
            if (file.contains("../") || file.startsWith("/")) {
                System.out.println("=== BLOCKED by filter");
                return ResponseEntity.badRequest().build();
            }

            String decodedFile = java.net.URLDecoder.decode(file, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("=== DECODED: " + decodedFile);
            java.io.File targetFile = new java.io.File(uploadDir + "/" + decodedFile);
            System.out.println("=== FULL PATH: " + targetFile.getAbsolutePath());
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
}