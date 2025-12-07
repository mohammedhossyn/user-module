package com.usermodule.mapper.auth;

import com.usermodule.dto.auth.LoginHistoryResponseDTO;
import com.usermodule.model.auth.LoginHistoryEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class LoginHistoryDTOMapper implements Function<LoginHistoryEntity, LoginHistoryResponseDTO> {

    @Override
    public LoginHistoryResponseDTO apply(@NonNull LoginHistoryEntity loginHistoryEntity) {
        return LoginHistoryResponseDTO.builder()
                .loginHistoryId(loginHistoryEntity.getLoginHistoryId())
                .hostAddress(loginHistoryEntity.getHostAddress())
                .hostName(loginHistoryEntity.getHostName())
                .username(loginHistoryEntity.getUserName())
                .requestIp(loginHistoryEntity.getRequestIp())
                .createdDate(loginHistoryEntity.getCreatedDate())
                .build();
    }
    
}