package com.example.fileupload.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data @NoArgsConstructor @RequiredArgsConstructor
public class Attachment {

    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NonNull
    private String fileName;

    @NonNull
    private String fileType;

    @NonNull @Lob
    private byte[] data;
}
