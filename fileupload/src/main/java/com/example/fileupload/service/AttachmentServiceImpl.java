package com.example.fileupload.service;

import com.example.fileupload.entity.Attachment;
import com.example.fileupload.repository.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;



@Service
public class AttachmentServiceImpl implements AttachmentService{

    private AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile multipartFile) throws Exception {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            if (fileName.contains(".."))
                throw new Exception("Filename Contains Invalid Path Sequence" + fileName);
            Attachment attachment = new Attachment(fileName, multipartFile.getContentType(), multipartFile.getBytes());
            return attachmentRepository.save(attachment);
        }catch (Exception e) {
            throw new Exception("Could not Save File: " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));
    }
}
