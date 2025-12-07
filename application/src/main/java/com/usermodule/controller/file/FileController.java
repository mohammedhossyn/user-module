package com.usermodule.controller.file;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.attachment.AttachmentService;
import com.usermodule.service.common.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final ApiResponseInspector apiResponseInspector;
    private final AttachmentService attachmentService;

    @PostMapping(value = "/upload/list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDTO uploadFiles(@RequestPart("files") MultipartFile[] files,
                                 @RequestParam("category") String category) {
        var list = fileService.uploadFiles(files, category);
        return apiResponseInspector.apiResponseBuilder(list, "");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDTO upload(@RequestPart("file") MultipartFile file,
                                 @RequestParam("category") String category) {
        var list = fileService.uploadFile(file, category);
        return apiResponseInspector.apiResponseBuilder(list, "");
    }

    @GetMapping(value = "/download")
    public ApiResponseDTO download(@NonNull @RequestParam("path") String path) {
        byte[] bytes = fileService.downloadFile(path);
        String file = Base64.getEncoder().encodeToString(bytes);
        return apiResponseInspector.apiResponseBuilder(file, "");
    }

    @GetMapping("/all/{username}")
    public ApiResponseDTO allByUsername(@NonNull @PathVariable String username) {
        var attachmentsResponseDTO = attachmentService.findAllByUser(username);
        return apiResponseInspector.apiResponseBuilder(attachmentsResponseDTO, "");
    }
}
