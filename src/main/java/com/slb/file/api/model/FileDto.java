package com.slb.file.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class FileDto {
    private String fileName;
    private String extension;
    private long size;
    private String path;
    private LocalDateTime modicationDate;
}
