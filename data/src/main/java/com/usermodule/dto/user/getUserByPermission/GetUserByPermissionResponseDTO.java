package com.usermodule.dto.user.getUserByPermission;

import lombok.Builder;

@Builder
public record GetUserByPermissionResponseDTO(Long id,
                                             String fullName) {
}
