package com.usermodule.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtil {

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private Optional<String> removeExtension(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.lastIndexOf('.') != -1)
                .map(f -> f.substring(0, f.lastIndexOf('.')));
    }

    public String getFileNameWithDate(String fileName, Date date) {
        return Optional.ofNullable(fileName)
                .filter(f -> getExtension(f).isPresent())
                .map(f -> removeExtension(f).orElse("")
                        .concat("_")
                        .concat(String.valueOf(date.getTime()))
                        .concat(".")
                        .concat(getExtension(f).orElse("")))
                .orElse(fileName);
    }

    public String getFileNameWithDate(MultipartFile[] files, Date date) {
        StringJoiner fileNames = new StringJoiner(",");
        Arrays.stream(files).forEach(file -> {
            String fileName = getFileNameWithDate(file.getOriginalFilename(), date);
            fileNames.add(fileName);
        });
        return fileNames.toString();
    }

    public String getFileName(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> !f.isEmpty())
                .filter(f -> f.lastIndexOf('\\') != -1)
                .map(f -> f.split("\\\\")[f.split("\\\\").length - 1])
                .orElse(fileName);
    }
}
