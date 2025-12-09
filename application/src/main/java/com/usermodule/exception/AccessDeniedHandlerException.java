package com.usermodule.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.usermodule.dto.common.ApiErrorResponseDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.utils.ResourceBundleUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerException implements AccessDeniedHandler {

    private final ResourceBundleUtil resourceBundleUtil;
    private final ApiResponseInspector apiResponseInspector;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        response.setHeader("content-type", "application/json");
        response.setStatus(200);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ApiErrorResponseDTO> errors = new ArrayList<>();
        errors.add(ApiErrorResponseDTO.builder()
                .message(resourceBundleUtil.getMessage("err.accessDenied"))
                .name(null)
                .code(401)
                .field(null)
                .value(null)
                .description(request.getRequestURI())
                .build());
        var appResponseDto = apiResponseInspector.apiResponseBuilder(errors, "",
                false);
        response.getWriter().write(mapper.writeValueAsString(appResponseDto));
    }
}
