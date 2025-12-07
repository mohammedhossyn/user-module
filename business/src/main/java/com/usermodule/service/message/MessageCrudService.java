package com.usermodule.service.message;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.message.MessageEntity;
import com.usermodule.repository.message.MessageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageCrudService {

    private final MessageRepository messageRepository;

    public MessageEntity create(@NonNull MessageEntity messageEntity) {
        log.debug("MessageCrudService.create started");
        if (messageEntity.getMessageId() == null) {
            return messageRepository.save(messageEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public MessageEntity read(@NonNull Long id) {
        log.debug("MessageCrudService.read started");
        Optional<MessageEntity> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public MessageEntity update(@NonNull MessageEntity messageEntity) {
        log.debug("MessageCrudService.update started");
        if (messageRepository.existsById(messageEntity.getMessageId())) {
            return messageRepository.save(messageEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull MessageEntity messageEntity) {
        log.debug("MessageCrudService.delete started");
        if (messageRepository.existsById(messageEntity.getMessageId())) {
            messageRepository.delete(messageEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public MessageEntity saveOrUpdate(@NonNull MessageEntity messageEntity) {
        log.debug("MessageCrudService.saveOrUpdate started");
        return messageRepository.save(messageEntity);
    }
}
