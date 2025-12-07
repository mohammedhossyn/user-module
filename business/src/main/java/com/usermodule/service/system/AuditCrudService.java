package com.usermodule.service.system;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.system.AuditEntity;
import com.usermodule.repository.system.AuditRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditCrudService {

    private final AuditRepository auditRepository;

    public AuditEntity create(@NonNull AuditEntity auditEntity) {
        log.debug("AuditCrudService.create started");
        if (auditEntity.getAuditId() == null) {
            return auditRepository.save(auditEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public AuditEntity read(@NonNull Long id) {
        log.debug("AuditCrudService.read started");
        Optional<AuditEntity> optionalAudit = auditRepository.findById(id);
        if (optionalAudit.isPresent()) {
            return optionalAudit.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public AuditEntity update(@NonNull AuditEntity auditEntity) {
        log.debug("AuditCrudService.update started");
        if (auditRepository.existsById(auditEntity.getAuditId())) {
            return auditRepository.save(auditEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull AuditEntity auditEntity) {
        log.debug("AuditCrudService.delete started");
        if (auditRepository.existsById(auditEntity.getAuditId())) {
            auditRepository.delete(auditEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public AuditEntity saveOrUpdate(@NonNull AuditEntity auditEntity) {
        log.debug("AuditCrudService.saveOrUpdate started");
        return auditRepository.save(auditEntity);
    }
}
