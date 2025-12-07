package com.usermodule.service.system;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.usermodule.dto.audit.AuditActionByResponseDTO;
import com.usermodule.dto.audit.AuditFieldDTO;
import com.usermodule.dto.audit.AuditResponseDTO;
import com.usermodule.model.system.AuditEntity;
import com.usermodule.model.system.OperationType;
import com.usermodule.repository.system.AuditRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditCrudService auditCrudService;
    private final AuditRepository auditRepository;

    public AuditEntity add(@NonNull AuditEntity auditEntity) {
        log.debug("AuditService.add started");
        return auditCrudService.create(auditEntity);
    }

    public List<AuditResponseDTO> search(@NonNull String entity, @NonNull Long referenceId) {
        log.debug("AuditService.search started");
        List<AuditResponseDTO> list = new ArrayList<>();

        List<AuditEntity> auditEntities = auditRepository
                .findByEntityAndReferenceIdOrderByActionDateAsc(entity, referenceId);
        AuditEntity firstAudit;
        AuditEntity secondAudit;
        String firstPayload;
        String secondPayload;
        JsonElement firstElement;
        JsonElement secondElement;
        JsonObject firstObject;
        JsonObject secondObject;
        List<AuditFieldDTO> auditFieldsDTO;
        JsonElement firstValue;
        JsonElement secondValue;
        String originalValue;
        String newValue;
        log.debug("auditEntities : {}", auditEntities.size());
        if (!auditEntities.isEmpty()) {
            int i = 0;
            for (AuditEntity auditEntity : auditEntities) {
                if (auditEntity.getOperation().equals(OperationType.UPDATE)) {
                    log.debug("OperationType : UPDATE");
                    if (i >= auditEntities.size() - 1) {
                        break;
                    }

                    firstAudit =  auditEntities.get(i + 1);
                    secondAudit = auditEntity;

                    firstPayload = firstAudit.getPayload();
                    secondPayload = secondAudit.getPayload();

                    firstElement = JsonParser.parseString(firstPayload);
                    secondElement = JsonParser.parseString(secondPayload);

                    firstObject = firstElement.getAsJsonObject();
                    secondObject = secondElement.getAsJsonObject();

                    auditFieldsDTO = new ArrayList<>();
                    for (String field : firstObject.keySet()) {
                        if (field.equals("createdDate") || field.equals("createdBy") || field.equals("lastModifiedDate") ||
                                field.equals("lastModifiedBY") || field.equals("version") || field.equals("actionDate") ||
                                field.equals("userNameIdentifier") || field.endsWith("Entity")) {
                            continue;
                        }
                        firstValue = firstObject.get(field);
                        secondValue = secondObject.get(field);

                        originalValue = StringUtils.replace(secondValue.toString(), "\"", "");
                        newValue = StringUtils.replace(firstValue.toString(), "\"", "");
                        if (originalValue.equals("null")) {
                            originalValue = null;
                        } else {
                            if (field.equals("password")) {
                                originalValue = "******";
                            }
                        }
                        if (newValue.equals("null")) {
                            newValue = null;
                        } else {
                            if (field.equals("password")) {
                                newValue = "******";
                            }
                        }
                        if (!firstValue.equals(secondValue)) {
                            auditFieldsDTO.add(AuditFieldDTO.builder()
                                    .fieldName(field)
                                    .originalValue(originalValue)
                                    .newValue(newValue)
                                    .build());
                        }
                    }
                    if (!auditFieldsDTO.isEmpty()) {
                        addToAuditList(list, firstAudit, auditFieldsDTO);
                    }
                } else if (auditEntity.getOperation().equals(OperationType.INSERT)) {
                    log.debug("OperationType : INSERT");
                    firstAudit = auditEntity;
                    firstPayload = auditEntity.getPayload();
                    firstElement = JsonParser.parseString(firstPayload);
                    firstObject = firstElement.getAsJsonObject();
                    auditFieldsDTO = new ArrayList<>();
                    for (String field : firstObject.keySet()) {
                        if (field.equals("createdDate") || field.equals("createdBy") || field.equals("lastModifiedDate") ||
                                field.equals("lastModifiedBY") || field.equals("version") || field.equals("actionDate") ||
                                field.equals("userNameIdentifier") || field.endsWith("Entity")) {
                            continue;
                        }
                        firstValue = firstObject.get(field);
                        newValue = StringUtils.replace(firstValue.toString(), "\"", "");
                        if (newValue.equals("null")) {
                            newValue = null;
                        } else {
                            if (field.equals("password")) {
                                newValue = "******";
                            }
                        }

                        auditFieldsDTO.add(AuditFieldDTO.builder()
                                .fieldName(field)
                                .originalValue(null)
                                .newValue(newValue)
                                .build());
                    }
                    addToAuditList(list, firstAudit, auditFieldsDTO);
                }
                i++;
            }
        }
        log.debug("AuditService.search ended");
        return list;
    }

    private void addToAuditList(List<AuditResponseDTO> list, AuditEntity firstAudit, List<AuditFieldDTO> auditFieldsDTO) {
        log.debug("AuditService.addToAuditList started");
        AuditActionByResponseDTO auditActionByResponseDTO = null;
        if (firstAudit.getActionBy() != null) {
            var user = firstAudit.getActionBy();
            auditActionByResponseDTO = AuditActionByResponseDTO.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .status(user.getStatus().getLabel())
                    .build();
        }
        list.add(AuditResponseDTO.builder()
                .actionDate(firstAudit.getActionDate())
                .actionBy(auditActionByResponseDTO)
                .operation(firstAudit.getOperation().getLabel())
                .fields(auditFieldsDTO)
                .build());
        log.debug("AuditService.addToAuditList ended");

    }
}