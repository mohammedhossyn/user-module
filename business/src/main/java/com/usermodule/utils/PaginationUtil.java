package com.usermodule.utils;

import com.usermodule.dto.common.PageHeaderResponseDTO;
import com.usermodule.dto.common.PageResponseWithFullHeaderDTO;
import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaginationUtil<T> {

    private final ResourceBundleUtil resourceBundleUtil;

    public PageResponseWithHeaderDTO<T> getHeader(Page<T> page, Class<T> element) {
        PageResponseWithHeaderDTO<T> pageResponseWithHeaderDTO = new PageResponseWithHeaderDTO<>(null, page);
        if (element != null) {
            List<String> headers = new ArrayList<>();
            Field[] fields = element.getDeclaredFields();
            for (Field field : fields) {
                headers.add(field.getName());
            }
            pageResponseWithHeaderDTO = new PageResponseWithHeaderDTO<>(headers, page);
        }
        return pageResponseWithHeaderDTO;
    }

    public PageResponseWithFullHeaderDTO<T> getHeaderWithLabel(Page<T> page) {
        PageResponseWithFullHeaderDTO<T> pageResponseWithFullHeaderDTO = new PageResponseWithFullHeaderDTO<>(null, page);
        if (page.hasContent()) {
            List<PageHeaderResponseDTO> headers = new ArrayList<>();
            T element = page.getContent().get(0);
            Field[] fields = element.getClass().getDeclaredFields();
            for (Field field : fields) {
                headers.add(PageHeaderResponseDTO.builder()
                        .field(field.getName())
                        .label(resourceBundleUtil.getMessage(element.getClass().getSimpleName() + "." + field.getName()))
                        .build());
            }
            pageResponseWithFullHeaderDTO = new PageResponseWithFullHeaderDTO<>(headers, page);
        }
        return pageResponseWithFullHeaderDTO;
    }
}
