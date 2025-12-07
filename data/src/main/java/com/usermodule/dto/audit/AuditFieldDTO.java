package com.usermodule.dto.audit;

import lombok.Builder;

@Builder
public record AuditFieldDTO
        (String fieldName,
         String originalValue,
         String newValue) {
}