package com.usermodule.dto.common;

import lombok.Builder;

@Builder
public record ApiErrorResponseDTO
        (Long errorId,
         String title,
         String name,
         Integer code,
         String field,
         String message,
         Object value,
         String description,
         String path,
         String className,
         String methodName,
         int line) {
}
