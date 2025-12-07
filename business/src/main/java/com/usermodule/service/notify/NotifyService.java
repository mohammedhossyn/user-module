package com.usermodule.service.notify;

import com.usermodule.dto.event.EventAddRequestDTO;
import com.usermodule.service.event.EventService;
import com.usermodule.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotifyService {

    private final EventService eventService;
    private final SocketService socketService;

    public void pushNotify(EventAddRequestDTO eventAddRequestDTO) {
        log.debug("NotifyService.pushNotify started");
        var eventResponseDTO = eventService.add(eventAddRequestDTO);
        socketService.sendMessage(eventAddRequestDTO, eventResponseDTO);
    }
}
