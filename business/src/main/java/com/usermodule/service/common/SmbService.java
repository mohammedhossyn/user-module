package com.usermodule.service.common;

import com.usermodule.exception.BusinessException;
import com.usermodule.service.system.OptionService;
import jcifs.CIFSContext;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

import static com.usermodule.exception.CodeException.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmbService {

    private final CIFSContext cifsContext;
    private final OptionService optionService;
    @Value("${smbPath}")
    private String smbPath;

    public SmbFile smbFile(String url) throws MalformedURLException {
        log.debug("SmbService.smbFile started");
        String server = optionService.getStringValueByCode(smbPath);
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        return new SmbFile("smb:" + server + url + "/", cifsContext);
    }

    @SneakyThrows
    public String uploadFile(byte[] content, String url, String fileName) {
        log.debug("SmbService.uploadFile started");
        if (!exists(url)) {
            createDirectory(url);
        }
        url += "/" + fileName;
        SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile(url));
        smbFileOutputStream.write(content);
        smbFileOutputStream.close();
        return url;
    }

    @SneakyThrows
    public byte[] downloadFile(String url) {
        log.debug("SmbService.downloadFile started");
        if (!exists(url)) {
            throw new BusinessException(File.FILE_NOT_FOUND);
        }
        SmbFileInputStream smbFileInputStream = new SmbFileInputStream(smbFile(url));
        byte[] content = smbFileInputStream.readAllBytes();
        smbFileInputStream.close();
        return content;
    }

    @SneakyThrows
    public boolean exists(String url) {
        log.debug("SmbService.exists started");
        try (SmbFile smbFile = smbFile(url)) {
            return smbFile.exists();
        }
    }

    @SneakyThrows
    public void createDirectory(String url) {
        log.debug("SmbService.createDirectory started");
        try (SmbFile smbFile = smbFile(url)) {
            smbFile.mkdirs();
        }
    }

    @SneakyThrows
    public SmbFile[] getSmbFileList(String url) {
        log.debug("SmbService.getSmbFileList started");
        if (!exists(url)) {
            throw new BusinessException(File.FILE_NOT_FOUND);
        }
        try (SmbFile smbFile = smbFile(url)) {
            return smbFile.listFiles();
        }
    }

    @SneakyThrows
    public void delete(String url) {
        log.debug("SmbService.delete started");
        try (SmbFile smbFile = smbFile(url)) {
            if (smbFile.exists()) {
                smbFile.delete();
                log.debug("File deleted successfully. {}", url);
            } else {
                log.debug("File does not exist. {}", url);
            }
        } catch (Exception e) {
            log.debug("Error deleting file: {}", e.getMessage());
            throw new BusinessException("err.handleException");
        }
    }
}
