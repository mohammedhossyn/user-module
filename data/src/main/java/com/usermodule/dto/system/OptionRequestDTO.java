package com.usermodule.dto.system;

import lombok.Builder;

@Builder
public record OptionRequestDTO(
        String description,
        String code,
        String value) {
}
