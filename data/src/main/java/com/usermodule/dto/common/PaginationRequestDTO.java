package com.usermodule.dto.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public record PaginationRequestDTO
        (Integer page,
         Integer size,
         String[] sort) {

    public PaginationRequestDTO {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
    }

    public Pageable getPageable() {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort() != null) {
            for (String sort : sort()) {
                String[] sorts = sort.split("-");
                if (sorts.length > 1) {
                    if (sorts[1].trim().equals("desc")) {
                        orders.add(Sort.Order.desc(sorts[0].trim()));
                    } else if (sorts[1].trim().equals("asc")) {
                        orders.add(Sort.Order.asc(sorts[0].trim()));
                    }
                } else {
                    orders.add(Sort.Order.by(sorts[0].trim()));
                }
            }
        }
        return PageRequest.of(page(), size(), Sort.by(orders));
    }
}
