package com.usermodule.dto.system;

import lombok.Builder;

import java.util.Date;


@Builder

public record ErrorResponseDTO(
        Long errorId,
        String title,
        String name,
        Integer code,
        String field,
        String message,
        String value,
        String description,
        String path,
        String className,
        String methodName,
        Integer line,
        String requestIp,
        String hostAddress,
        String hostName,
        Boolean isOpenTrans,
        Long version,
        String username,
        Long userId,
        Date createdDate,
        String stackTrace
) {
}
