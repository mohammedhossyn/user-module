package com.usermodule.service.auth;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.auth.LoginHistoryEntity;
import com.usermodule.repository.auth.LoginHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginHistoryCrudService {

    private final LoginHistoryRepository loginHistoryRepository;

    public LoginHistoryEntity create(@NonNull LoginHistoryEntity loginHistoryEntity) {
        log.debug("LoginHistoryCrudService.create started");
        if (loginHistoryEntity.getLoginHistoryId() == null) {
            return loginHistoryRepository.save(loginHistoryEntity);
        } else {
            throw new BusinessException(0);
        }
    }
}
