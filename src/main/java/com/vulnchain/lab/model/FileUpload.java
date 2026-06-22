package com.vulnchain.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.aot.AotServices;

import java.time.LocalDateTime;

@Entity
@Table( name = "file_uploads")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column ( name = "origin_name")
    private String originName;

    @Column ( name = "stored_path")
    private String storedPath;

    @Column ( name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn ( name = "uploaded_by")
    private User uploadedBy;

    @Column ( name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

}
