package com.usermodule.dto.user.permission;

import java.util.List;

public record PermissionTreeResponseDTO
        (Long permissionId,
         String code,
         String description,
         List<PermissionTreeResponseDTO> children) {
    public PermissionTreeResponseDTO withChild(List<PermissionTreeResponseDTO> children) {
        return new PermissionTreeResponseDTO(permissionId(), code(), description(), children);
    }
}
