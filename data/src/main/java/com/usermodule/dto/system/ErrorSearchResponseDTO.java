package com.usermodule.dto.system;

import lombok.Builder;

import java.util.Date;
@Builder
public record ErrorSearchResponseDTO(
        Long errorId,
        String username,
        Integer code,
        String value,
        String message,
        String description,
        String requestIp,
        String hostAddress,
        Date createdDate) {
}
