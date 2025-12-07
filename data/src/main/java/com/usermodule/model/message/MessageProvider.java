package com.usermodule.model.message;

public interface MessageProvider {
        boolean sendSms(MessageEntity messageEntity);
        String getProviderName();
}
