package com.usermodule.service.socket;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.socket.SocketIOEntity;
import com.usermodule.repository.socket.SocketIORepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class SocketCrudService {

    private final SocketIORepository socketIORepository;

    public SocketIOEntity create(@NonNull SocketIOEntity socketIOEntity) {
        log.debug("SocketCrudService.create started");
        if (socketIOEntity.getSocketIOId() == null) {
            return socketIORepository.save(socketIOEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public SocketIOEntity read(@NonNull Long id) {
        log.debug("SocketCrudService.read started");
        Optional<SocketIOEntity> socketIOEntity = socketIORepository.findById(id);
        if (socketIOEntity.isPresent()) {
            return socketIOEntity.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public SocketIOEntity update(@NonNull SocketIOEntity socketIOEntity) {
        log.debug("SocketCrudService.update started");
        if (socketIORepository.existsById(socketIOEntity.getSocketIOId())) {
            return socketIORepository.save(socketIOEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull SocketIOEntity socketIOEntity) {
        log.debug("SocketCrudService.delete started");
        if (socketIORepository.existsById(socketIOEntity.getSocketIOId())) {
            socketIORepository.delete(socketIOEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public SocketIOEntity saveOrUpdate(@NonNull SocketIOEntity socketIOEntity) {
        log.debug("SocketCrudService.saveOrUpdate started");
        return socketIORepository.save(socketIOEntity);
    }
}

