package com.usermodule.service.attachment;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.attachment.AttachmentCategory;
import com.usermodule.model.attachment.AttachmentEntity;
import com.usermodule.repository.attachment.AttachmentRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentCrudService {
    private final AttachmentRepository attachmentRepository;


    @Transactional
    public AttachmentEntity create(@NonNull AttachmentEntity attachmentEntity) {
        log.debug("AttachmentCrudService.create started");
        if (attachmentEntity.getAttachmentId() == null) {
            return attachmentRepository.save(attachmentEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    @Transactional
    public AttachmentEntity read(@NonNull AttachmentEntity attachmentEntity) {
        log.debug("AttachmentCrudService.read started");
        Optional<AttachmentEntity> optionalAttachment = attachmentRepository.findById(attachmentEntity.getAttachmentId());
        if (optionalAttachment.isPresent()) {
            return optionalAttachment.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public AttachmentEntity update(@NonNull AttachmentEntity attachmentEntity) {
        log.debug("AttachmentCrudService.update started");
        if (attachmentRepository.existsById(attachmentEntity.getAttachmentId())) {
            return attachmentRepository.save(attachmentEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull AttachmentEntity attachmentEntity) {
        log.debug("AttachmentCrudService.delete started");
        if (attachmentRepository.existsById(attachmentEntity.getAttachmentId())) {
            attachmentRepository.delete(attachmentEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public AttachmentEntity saveOrUpdate(@NonNull AttachmentEntity attachmentEntity) {
        log.debug("AttachmentCrudService.saveOrUpdate started");
        return attachmentRepository.save(attachmentEntity);
    }

    public AttachmentEntity findAttachmentByUsernameAndCategory(String username, AttachmentCategory category) {
        log.debug("AttachmentCrudService.findAttachmentByUsernameAndCategory started");
        return attachmentRepository.findByUser_UsernameAndCategory(username, category);
    }

    public List<AttachmentEntity> findAllByUsername(String username) {
        log.debug("AttachmentCrudService.findAllByUsername started");
        return attachmentRepository.findAllByUser_Username(username);
    }
}
