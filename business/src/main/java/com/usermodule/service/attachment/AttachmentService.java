package com.usermodule.service.attachment;

import com.usermodule.dto.attachment.AttachmentResponseDTO;
import com.usermodule.mapper.attachment.AttachmentResponseDTOMapper;
import com.usermodule.model.attachment.AttachmentCategory;
import com.usermodule.model.attachment.AttachmentEntity;
import com.usermodule.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentCrudService attachmentCrudService;
    private final AttachmentResponseDTOMapper attachmentResponseDTOMapper;

    public AttachmentResponseDTO create(UserEntity user, AttachmentCategory category,
                                                      String fileName,
                                                      String filePath){
        log.debug("start checkAndCreate");
        AttachmentEntity attachment = AttachmentEntity.builder()
                .fileName(fileName)
                .filePath(filePath)
                .category(category)
                .user(user)
                .build();
        log.debug("end checkAndCreate");
        return attachmentResponseDTOMapper.apply(attachmentCrudService.create(attachment));
    }

    public String checkExistWithCategoryAndDelete(UserEntity user, AttachmentCategory category) {
        var attachment = findAttachmentByUsernameAndCategory(user.getUsername(), category);
        if (attachment != null){
            log.debug("there is a file with this info");
            String path = attachment.getFilePath();
            attachmentCrudService.delete(attachment);
            return path;
        } else {
            return null;
        }
    }

    public AttachmentEntity findAttachmentByUsernameAndCategory(String username, AttachmentCategory category) {
        return attachmentCrudService.findAttachmentByUsernameAndCategory(username, category);
    }

    public List<AttachmentResponseDTO> findAllByUser(String username) {
        List<AttachmentEntity> attachments = attachmentCrudService.findAllByUsername(username);
        return attachments.stream().map(attachmentResponseDTOMapper).collect(Collectors.toList()) ;
    }

}
