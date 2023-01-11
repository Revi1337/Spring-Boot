package com.example.fileupload2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Table(name = "ImageData")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob @Column(name = "imageData", length = 1000)
    private byte[] data;
}
