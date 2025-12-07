package com.usermodule.dto.user.permission;

public record PermissionSearchRequestDTO(Long roleId,
                                         String description,
                                         Long parentId) {
}
