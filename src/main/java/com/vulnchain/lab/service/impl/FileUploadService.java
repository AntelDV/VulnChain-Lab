package com.vulnchain.lab.service.impl;

import com.vulnchain.lab.model.FileUpload;
import com.vulnchain.lab.model.User;
import com.vulnchain.lab.repository.FileUploadRepository;
import com.vulnchain.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vulnchain.lab.dto.FileUploadResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final FileUploadRepository fileUploadRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // Blacklist — to bypass on Java/Tomcat
    private static final List<String> BLACKLIST = List.of("php", "php3", "php4", "php5", "phtml", "phar",
            "asp", "aspx", "sh", "bash", "py", "rb", "pl");

    public FileUpload uploadFile(MultipartFile file, String username) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        // Validate 1: null check
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Filename is required");
        }

        String sanitizerFileName = sanitizeFilename(originalFilename);

        // Validate 2: check extension against blacklist
        String extension = getExtension(originalFilename).toLowerCase();
        if ( BLACKLIST.contains(extension)) {
            throw new IllegalArgumentException("File type not allowed: " + extension);
        }

        // Validate 3: Check Content-Type
        if (contentType != null && contentType.contains("text/html")) {
            throw new SecurityException("HTML files not allowed");
        }

        Path uploadPath = Paths.get(uploadDir);
        if ( !Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        // Save file
        Path filePath = uploadPath.resolve(sanitizerFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata to DB
        User user = userRepository.findByUsername(username).orElse(null);

        FileUpload fileUpload = new FileUpload();
        fileUpload.setOriginName(originalFilename);
        fileUpload.setStoredPath(filePath.toString());
        fileUpload.setContentType(contentType);
        fileUpload.setUploadedBy(user);
        fileUpload.setUploadedAt(LocalDateTime.now());

        return fileUploadRepository.save(fileUpload);
    }

    private String getExtension(String filename){
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 ) return "";
        return filename.substring(dotIndex + 1);
    }

    public List<FileUpload> getUploadsByUser ( String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if ( user  == null) return List.of();
        return fileUploadRepository.findByUploadedBy(user);
    }

    private static final List<String> FILE_WHITELIST = List.of(
            "pdf", "docx", "xlsx", "pptx", "txt",
            "png", "jpg", "jpeg", "gif", "ico", "zip"
    );

    public List<FileUploadResponse> getAllUploads() {
        return fileUploadRepository.findAll().stream().filter(f -> {
                    String name = f.getOriginName();
                    if (name == null || name.contains("%")) return false;
                    String ext = name.contains(".")
                            ? name.substring(name.lastIndexOf('.') + 1).toLowerCase()
                            : "";
                    return FILE_WHITELIST.contains(ext);
                })
                .sorted((a, b) -> b.getUploadedAt().compareTo(a.getUploadedAt()))
                .map(f -> new FileUploadResponse(
                        f.getId(),
                        f.getOriginName(),
                        f.getContentType(),
                        f.getUploadedAt(),
                        f.getUploadedBy() != null ? f.getUploadedBy().getUsername() : "unknown"
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) return null;

        if (filename.contains("../") || filename.contains("..\\")
                || filename.startsWith("/")) {
            throw new SecurityException("Path traversal detected!");
        }

        try {
            filename = java.net.URLDecoder.decode(
                    filename, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
        }

        filename = filename.replace("../", "").replace("..\\", "");
        return filename;
    }

    public FileUpload uploadFileWithName(MultipartFile file, String customFilename, String username) throws IOException {

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new SecurityException("Only image files allowed");
        }

        if (customFilename.contains("../") || customFilename.startsWith("/")) {
            throw new SecurityException("Invalid filename");
        }

        // Decode SAU check
        String decoded = java.net.URLDecoder.decode(
                customFilename, java.nio.charset.StandardCharsets.UTF_8);

        String fullPath = uploadDir + "/" + decoded;
        java.io.File targetFile = new java.io.File(fullPath);

        Files.copy(file.getInputStream(), targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        User user = userRepository.findByUsername(username).orElse(null);
        FileUpload fileUpload = new FileUpload();
        fileUpload.setOriginName(customFilename);
        fileUpload.setStoredPath(fullPath);
        fileUpload.setContentType(contentType);
        fileUpload.setUploadedBy(user);
        fileUpload.setUploadedAt(LocalDateTime.now());

        return fileUploadRepository.save(fileUpload);
    }


}
