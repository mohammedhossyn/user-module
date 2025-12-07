package com.usermodule.dto.user.role;

import lombok.Builder;

@Builder
public record RoleAddResponseDTO
        (String name,
         String description) {
}
