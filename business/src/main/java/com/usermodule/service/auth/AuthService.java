package com.usermodule.service.auth;

import com.usermodule.dto.auth.LoginRequestDTO;
import com.usermodule.dto.auth.LoginResponseDTO;
import com.usermodule.dto.user.UserDetailsDTO;
import com.usermodule.service.security.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginHistoryService loginHistoryService;

    public LoginResponseDTO login(@NonNull LoginRequestDTO loginRequestDTO,String hostName, String hostAddress,
                                  String requestIp) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.username(),
                loginRequestDTO.password()));
        log.debug("isAuthenticated={}", authentication.isAuthenticated());
        var user = ((UserDetailsDTO)authentication.getPrincipal()).getUser();
        var token = jwtService.generateToken(user);
        log.debug("token={}", token);
        Date tokenExpirationDate = jwtService.extractExpiration(token);
        CompletableFuture.supplyAsync(() -> {
            try {
                loginHistoryService.add(user.getUserId(), user.getUsername(), hostName, hostAddress, requestIp);
                log.debug("add login History");
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return null;
        });
        return LoginResponseDTO.builder()
                .token(token)
                .expirationDate(tokenExpirationDate)
                .build();
    }
}
