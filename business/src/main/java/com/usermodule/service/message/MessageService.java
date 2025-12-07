package com.usermodule.service.message;

import com.usermodule.model.message.MessageProvider;
import com.usermodule.model.message.MessageEntity;
import com.usermodule.repository.message.MessageRepository;
import com.usermodule.service.system.OptionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.usermodule.model.message.MessageType.FORGOT_PASSWORD;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final OptionService optionService;
    private final List<MessageProvider> providers;
    private MessageProvider activeProvider;

    @Value("${sms.active-provider}")
    private String activeProviderName;

    private static final String FORGET_PASSWORD_TEXT_OPTION = "FORGET_PASSWORD_MESSAGE_TEXT";

    @PostConstruct
    public void init() {
        log.debug("MessageService.init started");
        this.activeProvider = providers.stream()
                .filter(p -> p.getProviderName().equals(activeProviderName))
                .findFirst()
                .orElse(providers.get(0));
    }


    public MessageEntity fillMessageOfForgetPassword(String code,
                                                                 String recipient){
        log.debug("MessageService.fillMessageOfForgetPassword started");
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        return MessageEntity.builder()
                .message(getMessageTextReady(FORGET_PASSWORD_TEXT_OPTION, params))
                .sender(activeProviderName)
                .recipient(recipient)
                .isSent(false)
                .type(FORGOT_PASSWORD)
                .build();
    }

    public MessageEntity sendMessageOfForgetPassword(String code, String recipient){
        log.debug("MessageService.sendMessageOfForgetPassword started");
        var messageEntity = fillMessageOfForgetPassword(code, recipient);
        boolean isSent = sendSms(messageEntity);
        messageEntity.setIsSent(isSent);
        messageRepository.save(messageEntity);
        if (isSent) return messageEntity;
        else throw new RuntimeException();
    }

    public String getMessageOption(String code){
        log.debug("MessageService.getMessageOption started");
        return optionService.getStringValueByCode(code);
    }

    public String getMessageTextReady(String code, Map<String, Object> params){
        log.debug("MessageService.getMessageTextReady started");
        String messageText = getMessageOption(code);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            messageText = messageText.replace("{" + k + "}", v.toString());
        }
        return messageText;
    }

    public boolean sendSms(MessageEntity messageEntity) {
        log.debug("MessageService.sendSms started");
        if (activeProvider == null) {
            throw new IllegalStateException("No active SMS provider configured.");
        }
        return activeProvider.sendSms(messageEntity);
    }

    public void setActiveProvider(String providerName) {
        log.debug("MessageService.setActiveProvider started");
        activeProvider = providers.stream()
                .filter(p -> p.getProviderName().equals(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provider not found: " + providerName));
    }

}
