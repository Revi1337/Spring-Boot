package com.example.fileupload2.service;

import com.example.fileupload2.entity.ImageData;
import com.example.fileupload2.repository.StorageRepository;
import com.example.fileupload2.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    private StorageRepository storageRepository;

    public StorageServiceImpl(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws Exception {

        ImageData imageData = storageRepository.save(
                ImageData.builder()
                        .name(multipartFile.getName())
                        .type(multipartFile.getContentType())
                        .data(ImageUtils.compressImage(multipartFile.getBytes()))
                        .build()
        );
        if (imageData != null) {
            return "file uploaded successfully " + multipartFile.getOriginalFilename();
        }
        return null;

    }

    @Override
    public byte[] downloadImage(String fileName) throws Exception{
        Optional<ImageData> dbImageData = storageRepository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getData());
        return images;
    }
}