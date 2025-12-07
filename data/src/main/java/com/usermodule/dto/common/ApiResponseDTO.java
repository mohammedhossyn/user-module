package com.usermodule.dto.common;

import lombok.Builder;

import java.util.Date;
import java.util.List;


@Builder
public record ApiResponseDTO
        (Boolean status,
         Object data,
         List<ApiErrorResponseDTO> errors,
         Date timestamp,
         String message) {
}
