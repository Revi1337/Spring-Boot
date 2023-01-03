package com.example.fileupload.service;


import com.example.fileupload.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    Attachment saveAttachment(MultipartFile multipartFile) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;
}
