package com.usermodule.service.common;

import com.usermodule.dto.attachment.AttachmentResponseDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.model.attachment.AttachmentCategory;
import com.usermodule.service.attachment.AttachmentService;
import com.usermodule.service.user.UserService;
import com.usermodule.utils.DateUtil;
import com.usermodule.utils.FileUtil;
import com.usermodule.utils.TransactionUtil;
import jakarta.transaction.Transactional;
import jcifs.smb.SmbFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final SmbService smbService;
    private final FileUtil fileUtil;
    private final DateUtil dateUtil;
    private final FileCategoryService fileCategoryService;
    private final AttachmentService attachmentService;
    private final TransactionUtil transactionUtil;
    private final UserService userService;

    @Transactional
    public List<AttachmentResponseDTO> uploadFiles(MultipartFile[] files, String category) {
        log.debug("FileService.uploadFiles started");
        AttachmentCategory attachmentCategory;
        var user = userService.getCurrentUser();
        try {
            attachmentCategory = AttachmentCategory.valueOf(category);
        } catch (Exception e) {
            throw BusinessException.builder()
                    .name("upload file")
                    .message("err.file.category.not.exists")
                    .build();
        }
        try {
            transactionUtil.openTransaction();
            String username = user.getUsername();
            List<AttachmentResponseDTO> attachments = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                if (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
                    String fileName = file.getOriginalFilename();
                    String url = category;
                    if (username != null) {
                        fileName = username + "_" + fileName;
                        url = url + "/" + username;
                    }
                    try {
                        String filePath = smbService.uploadFile(file.getBytes(), url, fileName);
                        String deletingPath = attachmentService.checkExistWithCategoryAndDelete(user, attachmentCategory);
                        smbService.delete(deletingPath);
                        var response = attachmentService.create(user, attachmentCategory, fileName, filePath);
                        attachments.add(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            log.debug("FileService.uploadFiles ended");
            transactionUtil.commit();
            return attachments;
        } catch (Exception e) {
            transactionUtil.rollback();
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public AttachmentResponseDTO uploadFile(MultipartFile file, String category) {
        log.debug("FileService.uploadFile started");
        AttachmentCategory attachmentCategory;
        var user = userService.getCurrentUser();
        try {
            attachmentCategory = AttachmentCategory.valueOf(category);
        } catch (Exception e) {
            throw BusinessException.builder()
                    .name("upload file")
                    .message("err.file.category.not.exists")
                    .build();
        }
        try {
            transactionUtil.openTransaction();
            String username = user.getUsername();
            if (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
                String fileName = file.getOriginalFilename();
                String url = category;
                if (username != null) {
                    fileName = username + "_" + fileName;
                    url = url + "/" + username;
                }
                try {
                    String filePath = smbService.uploadFile(file.getBytes(), url, fileName);
                    String deletingPath = attachmentService.checkExistWithCategoryAndDelete(user, attachmentCategory);
                    smbService.delete(deletingPath);
                    var response = attachmentService.create(user, attachmentCategory, fileName, filePath);
                    log.debug("FileService.uploadFile ended");
                    transactionUtil.commit();
                    return response;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new BusinessException("no.file.attached");
            }
        } catch (Exception e) {
            transactionUtil.rollback();
            throw new BusinessException(e.getMessage());
        }
    }

    @SneakyThrows
    public byte[] downloadFile(String path) {
        log.debug("FileService.downloadFile started");
        log.debug("path {}", path);
        return smbService.downloadFile(path);
    }

    @SneakyThrows
    public List<String> getFilePathList(String path) {
        log.debug("FileService.getFilePathList started");
        return Arrays.stream(smbService.getSmbFileList(path)).map(SmbFile::getUncPath)
                .map(pathFile -> pathFile.replace("\\", "/")).toList();
    }

}
