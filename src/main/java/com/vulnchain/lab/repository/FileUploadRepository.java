package com.vulnchain.lab.repository;

import com.vulnchain.lab.model.User;
import com.vulnchain.lab.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    List<FileUpload> findByUploadedBy (User uploadedBy);
}
