package com.example.fileupload2.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadImage(MultipartFile multipartFile) throws Exception;

    byte[] downloadImage(String fileName) throws Exception;

}
