package com.usermodule.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.usermodule.dto.event.EventAddRequestDTO;
import com.usermodule.dto.event.EventResponseDTO;
import com.usermodule.dto.socket.SocketRegisterRequestDTO;
import com.usermodule.model.socket.SocketIOEntity;
import com.usermodule.model.socket.SocketIOStatus;
import com.usermodule.repository.socket.SocketIORepository;
import com.usermodule.service.user.UserCrudService;
import com.usermodule.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.usermodule.model.socket.SocketIOStatus.ACTIVE;

@Service
@Slf4j
public class SocketService {

    @Value("${event-name}")
    private String eventName;
    private final SocketIOServer socketIOServer;
    private SocketCrudService socketCrudService;
    private UserCrudService userCrudService;
    private SocketIORepository socketIORepository;
    private final DateUtil dateUtil;


    public SocketService(SocketIOServer socketIOServer, SocketCrudService socketCrudService,
                         UserCrudService userCrudService, SocketIORepository socketIORepository,
                         DateUtil dateUtil) throws UnknownHostException {
        this.socketIOServer = socketIOServer;
        this.dateUtil = dateUtil;
        this.socketIOServer.addConnectListener(onUserConnectWithSocket);
        this.socketIOServer.addDisconnectListener(onUserDisconnectWithSocket);
        this.socketCrudService = socketCrudService;
        this.userCrudService = userCrudService;
        this.socketIORepository = socketIORepository;
        this.socketIOServer.addEventListener("connectUser", SocketRegisterRequestDTO.class, connectUser);
        this.socketIOServer.addEventListener("disconnectUser", SocketRegisterRequestDTO.class, disconnectUser);
        socketIORepository.deActiveAllSocket(InetAddress.getLocalHost().getHostAddress());
    }

    public ConnectListener onUserConnectWithSocket = client -> log.debug("onUserConnectWithSocket, uuid={}", client.getSessionId());

    public DisconnectListener onUserDisconnectWithSocket = client -> {
        log.debug("onUserDisconnectWithSocket, uuid={}", client.getSessionId());

        Optional<SocketIOEntity> optionalSocketIOEntity;
        try {
            optionalSocketIOEntity = socketIORepository.findBySessionIdAndServerName(String.valueOf(client.getSessionId()),
                    InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        if (optionalSocketIOEntity.isPresent()) {
            var socketIOEntity= optionalSocketIOEntity.get();
            socketIOEntity.setStatus(SocketIOStatus.INACTIVE);
            socketCrudService.update(socketIOEntity);
        }
    };

    public void sendEvent(UUID uuid, EventResponseDTO eventResponseDTO) {
        log.debug("SocketService.sendEvent started");
        SocketIOClient socketIOClient = getClientByUuid(uuid);
        if (socketIOClient != null) {
            socketIOClient.sendEvent(eventName, eventResponseDTO);
        }
    }

    private SocketIOClient getClientByUuid(UUID uuid) {
        log.debug("SocketService.getClientByUuid started");
        return socketIOServer.getClient(uuid);
    }

    public DataListener<SocketRegisterRequestDTO> connectUser = (client, socketRegisterRequestDTO, acknowledge) -> {
        log.debug("SocketService.connectUser started");
        log.debug("connectUser, uuid={}", client.getSessionId());
        var socketIOEntityOptional = socketIORepository.findBySessionIdAndServerName(String.valueOf(client.getSessionId()),
                InetAddress.getLocalHost().getHostAddress());
        if (socketIOEntityOptional.isEmpty()) {
            if (socketRegisterRequestDTO != null && socketRegisterRequestDTO.loginUserId() != null) {
                log.debug("socketRegisterRequestDTO.loginUserId()={}", socketRegisterRequestDTO.loginUserId());
                var UserEntity = userCrudService.read(socketRegisterRequestDTO.loginUserId());
                SocketIOEntity socketIOEntity;
                socketIOEntity = SocketIOEntity.builder()
                        .sessionId(String.valueOf(client.getSessionId()))
                        .status(SocketIOStatus.ACTIVE)
                        .user(UserEntity)
                        .username(UserEntity.getUsername())
                        .serverName(InetAddress.getLocalHost().getHostAddress())
                        .build();
                socketCrudService.create(socketIOEntity);
                // this is a sample response to client
                acknowledge.sendAckData(socketRegisterRequestDTO);
            }
        }
    };

    public DataListener<SocketRegisterRequestDTO> disconnectUser = (client, message, acknowledge) -> {
        log.debug("disconnectUser, uuid={}", client.getSessionId());
        client.disconnect();
    };

    public void sendMessage(EventAddRequestDTO eventAddRequestDTO, EventResponseDTO eventResponseDTO){
        log.debug("SocketService.sendMessage started");
        CompletableFuture.supplyAsync(() -> {
            try {
                EventResponseDTO responseDTO = EventResponseDTO.builder()
                        .title(eventResponseDTO.title())
                        .message(eventResponseDTO.message())
                        .status(eventResponseDTO.status())
                        .referenceId(eventResponseDTO.referenceId())
                        .userId(eventResponseDTO.userId())
                        .type(eventResponseDTO.type())
                        .eventId(eventResponseDTO.eventId())
                        .createdDateStr(dateUtil.getSolarDateByFormat(eventResponseDTO.createdDate(), "yyyy/MM/dd"))
                        .createdTimeStr(dateUtil.getSolarDateByFormat(eventResponseDTO.createdDate(), "hh:mm:ss"))
                        .build();
                var socketIOEntityList = socketIORepository
                        .findByUsernameAndStatus(eventAddRequestDTO.user().getUsername(), ACTIVE);
                for (SocketIOEntity socketIOEntity : socketIOEntityList) {
                    sendEvent(UUID.fromString(socketIOEntity.getSessionId()), responseDTO);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.debug("SocketService.sendMessage ended");
            return null;
        });
    }
}
