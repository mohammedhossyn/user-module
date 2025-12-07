package com.usermodule.dto.audit;

import lombok.Builder;

@Builder
public record AuditActionByResponseDTO
        (Long userId,
         String username,
         String firstName,
         String surname,
         String status) {
}
