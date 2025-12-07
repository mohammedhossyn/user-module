package com.usermodule.controller.enums;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.model.event.EventStatus;
import com.usermodule.model.event.EventType;
import com.usermodule.model.message.MessageType;
import com.usermodule.model.socket.SocketIOStatus;
import com.usermodule.model.system.OperationType;
import com.usermodule.model.user.InquiryStatus;
import com.usermodule.model.user.UserStatus;
import com.usermodule.service.enums.EnumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/enum")
public class EnumController {

    private final EnumService enumService;
    private final ApiResponseInspector apiResponseInspector;

    @GetMapping("/user/status")
    public ApiResponseDTO getUserStatus() {
        var enumsResponseDTO = enumService.getAllByType(UserStatus.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }

    @GetMapping("/event/status")
    public ApiResponseDTO getEventStatus() {
        var enumsResponseDTO = enumService.getAllByType(EventStatus.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }

    @GetMapping("/event/type")
    public ApiResponseDTO getEventType() {
        var enumsResponseDTO = enumService.getAllByType(EventType.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }


    @GetMapping("/message/type")
    public ApiResponseDTO getMessageType() {
        var enumsResponseDTO = enumService.getAllByType(MessageType.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }

    @GetMapping("/socketIO/status")
    public ApiResponseDTO getSocketIOStatus() {
        var enumsResponseDTO = enumService.getAllByType(SocketIOStatus.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }

    @GetMapping("/inquiry/status")
    public ApiResponseDTO getInquiryStatus() {
        var enumsResponseDTO = enumService.getAllByType(InquiryStatus.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }

    @GetMapping("/operation/type")
    public ApiResponseDTO getOperationType() {
        var enumsResponseDTO = enumService.getAllByType(OperationType.class);
        return apiResponseInspector.apiResponseBuilder(enumsResponseDTO, "");
    }
}
