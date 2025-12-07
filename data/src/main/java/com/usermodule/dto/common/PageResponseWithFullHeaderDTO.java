package com.usermodule.dto.common;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PageResponseWithFullHeaderDTO<T>
        (List<PageHeaderResponseDTO> headers,
         Page<T> page) {
}
