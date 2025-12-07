package com.usermodule.dto.user.permission;

import lombok.Builder;

@Builder
public record PermissionResponseDTO
        (Long permissionId,
         String code,
         String description,
         PermissionResponseDTO permissionParent) {
}