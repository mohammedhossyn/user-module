package com.usermodule.service.event;


import com.usermodule.exception.BusinessException;
import com.usermodule.model.event.EventEntity;
import com.usermodule.repository.event.EventRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class EventCrudService {
    private final EventRepository eventRepository;

    public EventEntity create(@NonNull EventEntity eventEntity) {
        log.debug("EventCrudService.create started");
        if (eventEntity.getEventId() == null) {
            return eventRepository.save(eventEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public EventEntity read(@NonNull Long id) {
        log.debug("EventCrudService.read started");
        Optional<EventEntity> eventEntity = eventRepository.findById(id);
        if (eventEntity.isPresent()) {
            return eventEntity.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public EventEntity update(@NonNull EventEntity eventEntity) {
        log.debug("EventCrudService.update started");
        if (eventRepository.existsById(eventEntity.getEventId())) {
            return eventRepository.save(eventEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull EventEntity eventEntity) {
        log.debug("EventCrudService.delete started");
        if (eventRepository.existsById(eventEntity.getEventId())) {
            eventRepository.delete(eventEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public EventEntity saveOrUpdate(@NonNull EventEntity eventEntity) {
        log.debug("EventCrudService.saveOrUpdate started");
        return eventRepository.save(eventEntity);
    }
}


