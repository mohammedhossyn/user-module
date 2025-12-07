package com.usermodule.dto.system;

import lombok.Builder;

@Builder
public record ErrorSearchRequestDTO(
        Long errorId,
        String username,
        Integer code,
        String requestIp,
        String hostAddress,
        String createdDateFrom,
        String createdDateTo,
        String path) {
}
