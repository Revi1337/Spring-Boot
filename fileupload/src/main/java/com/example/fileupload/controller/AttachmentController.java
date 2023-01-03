package com.example.fileupload.controller;

import com.example.fileupload.dto.ResponseData;
import com.example.fileupload.entity.Attachment;
import com.example.fileupload.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam(name = "file") MultipartFile multipartFile) throws Exception{
        Attachment attachment = null;
        String downloadURL = "";
        attachment = attachmentService.saveAttachment(multipartFile);
        downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseData(attachment.getFileName(), downloadURL, multipartFile.getContentType(), multipartFile.getSize());
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "fileId") String fileId) throws Exception{
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
}
