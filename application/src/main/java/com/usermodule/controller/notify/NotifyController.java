package com.usermodule.controller.notify;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.event.EventSearchRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.event.EventService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/notify")
public class NotifyController {
    private final EventService eventService;
    private final ApiResponseInspector apiResponseInspector;


    @PutMapping("/seen/{eventId}")
    public ApiResponseDTO seenEvent(@NonNull @PathVariable Long eventId) {
        boolean seenEventResponseDTO = eventService.seen(eventId);
        return apiResponseInspector.apiResponseBuilder(seenEventResponseDTO, "",
                true);
    }

    @PostMapping("/search")
    public ApiResponseDTO search(PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody EventSearchRequestDTO eventSearchRequestDTO) {
        var listDTO = eventService.search(paginationRequestDTO.getPageable(),
                eventSearchRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }
}
