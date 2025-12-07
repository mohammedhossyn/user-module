package com.usermodule.service.event;

import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.event.EventAddRequestDTO;
import com.usermodule.dto.event.EventResponseDTO;
import com.usermodule.dto.event.EventSearchRequestDTO;
import com.usermodule.model.event.EventEntity;
import com.usermodule.model.event.EventStatus;
import com.usermodule.model.event.EventType;
import com.usermodule.repository.event.EventRepository;
import com.usermodule.utils.DateUtil;
import com.usermodule.utils.PaginationUtil;
import com.usermodule.utils.ResourceBundleUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.usermodule.model.event.EventStatus.SEEN;
import static com.usermodule.model.event.EventStatus.UNSEEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventCrudService eventCrudService;
    private final ResourceBundleUtil resourceBundleUtil;
    private final PaginationUtil<EventResponseDTO> eventResponseDTOPaginationUtil;
    private final DateUtil dateUtil;

    public EventResponseDTO add(EventAddRequestDTO eventAddRequestDTO) {
        log.debug("EventService.add started");
        var eventEntity = EventEntity.builder()
                .title(EventType.valueOf(eventAddRequestDTO.type()).getLabel())
                .message(eventAddRequestDTO.message())
                .status(UNSEEN)
                .referenceId(eventAddRequestDTO.referenceId())
                .user(eventAddRequestDTO.user())
                .type(EventType.valueOf(eventAddRequestDTO.type()))
                .build();
        eventCrudService.create(eventEntity);
        return EventResponseDTO.builder()
                .title(eventEntity.getTitle())
                .message(eventEntity.getMessage())
                .status(eventEntity.getStatus().getLabel())
                .referenceId(eventEntity.getReferenceId())
                .userId(eventEntity.getUser().getUserId())
                .type(eventEntity.getType().getLabel())
                .eventId(eventEntity.getEventId())
                .createdDate(eventEntity.getCreatedDate())
                .build();
    }

    public boolean seen(@NonNull Long eventId) {
        log.debug("EventService.seen started");
        var eventEntity = eventCrudService.read(eventId);
        eventEntity.setStatus(SEEN);
        eventCrudService.update(eventEntity);
        return true;
    }

    public PageResponseWithHeaderDTO<EventResponseDTO> search(Pageable pageable,
                                                              EventSearchRequestDTO eventSearchRequestDTO) {
        log.debug("EventService.search started");
        Page<EventResponseDTO> pages = eventRepository.search(pageable,
                        eventSearchRequestDTO.userId(),
                        eventSearchRequestDTO.type(),
                        eventSearchRequestDTO.status(),
                        dateUtil.getDate(eventSearchRequestDTO.createdDateFrom()),
                        dateUtil.getDate(eventSearchRequestDTO.createdDateTo()))
                .map(eventEntity -> EventResponseDTO.builder()
                        .eventId(eventEntity.getEventId())
                        .title(eventEntity.getTitle())
                        .message(eventEntity.getMessage())
                        .type(eventEntity.getType().getLabel())
                        .status(eventEntity.getStatus().getLabel())
                        .referenceId(eventEntity.getReferenceId())
                        .createdDate(eventEntity.getCreatedDate())
                        .userId(eventEntity.getUser().getUserId())
                        .build());
        return eventResponseDTOPaginationUtil.getHeader(pages, EventResponseDTO.class);
    }
}
