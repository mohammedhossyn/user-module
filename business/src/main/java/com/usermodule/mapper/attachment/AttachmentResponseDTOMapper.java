package com.usermodule.mapper.attachment;

import com.usermodule.dto.attachment.AttachmentResponseDTO;
import com.usermodule.model.attachment.AttachmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AttachmentResponseDTOMapper implements Function<AttachmentEntity, AttachmentResponseDTO> {
    @Override
    public AttachmentResponseDTO apply(AttachmentEntity attachmentEntity) {
        return AttachmentResponseDTO.builder()
                .id(attachmentEntity.getAttachmentId())
                .fileName(attachmentEntity.getFileName())
                .filePath(attachmentEntity.getFilePath())
                .username(attachmentEntity.getUser().getUsername())
                .category(attachmentEntity.getCategory().toString())
                .build();
    }
}
