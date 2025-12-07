package com.usermodule.dto.user.permission;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record PermissionAddRequestDTO
        (@NotEmpty String code,
         @NotEmpty String description,
         Long parentId,
         Long roleId) {
}
