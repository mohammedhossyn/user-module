package com.usermodule.dto.enums;

import lombok.Builder;

@Builder
public record EnumResponseDTO(String value,
                              String label) {
}
