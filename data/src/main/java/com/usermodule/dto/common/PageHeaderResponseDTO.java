package com.usermodule.dto.common;

import lombok.Builder;

@Builder
public record PageHeaderResponseDTO
        (String field,
         String label) {
}
