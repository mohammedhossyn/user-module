package com.usermodule.dto.user.role;

import com.usermodule.dto.user.permission.PermissionResponseDTO;
import lombok.Builder;

import java.util.Set;

@Builder
public record RoleResponseDTO
        (Long roleId,
         String name,
         String description,
         Set<PermissionResponseDTO> permissions) {
}
