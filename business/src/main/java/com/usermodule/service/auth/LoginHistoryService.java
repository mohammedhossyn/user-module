package com.usermodule.service.auth;

import com.usermodule.dto.auth.LoginHistoryResponseDTO;
import com.usermodule.mapper.auth.LoginHistoryDTOMapper;
import com.usermodule.model.auth.LoginHistoryEntity;
import com.usermodule.repository.auth.LoginHistoryRepository;
import com.usermodule.repository.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final LoginHistoryDTOMapper loginHistoryDTOMapper;
    private final LoginHistoryCrudService loginHistoryCrudService;

    public void add(@NonNull Long userId, String username, String hostName, String hostAddress, String requestIp) {
        log.debug("LoginHistoryService.add started");
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            var loginHistory = LoginHistoryEntity.builder()
                    .user(user.get())
                    .userName(username)
                    .hostName(hostName)
                    .hostAddress(hostAddress)
                    .requestIp(requestIp)
                    .createdDate(new Date())
                    .sessionId("0")
                    .loginResult("0")
                    .build();
            loginHistoryCrudService.create(loginHistory);
        }
    }
    public Page<LoginHistoryResponseDTO> findByUserId(@NonNull Pageable pageable, @NonNull Long id) {
        log.debug("LoginHistoryService.findByUserId started");
        return userRepository.findById(id).map(userEntity -> loginHistoryRepository
                .findByUser(pageable, userEntity)
                .map(loginHistoryDTOMapper)).orElseGet(Page::empty);
    }
}
