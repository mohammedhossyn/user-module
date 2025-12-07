package com.usermodule.dto.audit;

import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public record AuditResponseDTO
        (AuditActionByResponseDTO actionBy,
         String operation,
         Date actionDate,
         List<AuditFieldDTO> fields) {
}