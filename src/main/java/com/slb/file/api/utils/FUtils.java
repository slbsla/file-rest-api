package com.slb.file.api.utils;

import com.slb.file.api.model.FileDto;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

public class FUtils {
    public static FileDto toDto(File file) {
        return FileDto.builder()
                .fileName(Optional.ofNullable(file.getName())
                            .filter(f -> f.contains("."))
                            .map(f -> f.substring(0,file.getName().indexOf("."))).get())
                .size(file.length())
                .path(file.getPath())
                .modicationDate(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime())
                .extension(Optional.ofNullable(file.getName())
                                   .filter(f -> f.contains("."))
                                   .map(f -> f.substring(file.getName().lastIndexOf(".") + 1)).get())
                .build();
    }


}
