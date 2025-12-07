package com.usermodule.dto.user.getUserByPermission;

import lombok.Builder;

import java.util.List;

@Builder
public record GetUserByPermissionRequestDTO(List<String> codes) {
}
