package com.usermodule.dto.common;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PageResponseWithHeaderDTO<T>
        (List<String> headers,
         Page<T> page) {
}
