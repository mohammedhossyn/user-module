package com.usermodule.model.message;

import org.springframework.stereotype.Component;

@Component
public class MessageProviderExample implements MessageProvider {
    @Override
    public boolean sendSms(MessageEntity messageEntity) {
        System.out.println("Sending SMS via Magfa to " + messageEntity.getSender() + ": " + messageEntity.getMessage());
        return true;
    }

    @Override
    public String getProviderName() {
        return "Magfa";
    }
}
