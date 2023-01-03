package com.example.fileupload.repository;

import com.example.fileupload.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {

}
