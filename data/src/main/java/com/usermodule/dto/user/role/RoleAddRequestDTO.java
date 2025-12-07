package com.usermodule.dto.user.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.Set;

@Builder
public record RoleAddRequestDTO
        (@NotEmpty String name,
         @NotEmpty String description,
         Set<Long> permissionsId) {
}
