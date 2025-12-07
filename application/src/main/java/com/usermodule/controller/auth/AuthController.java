package com.usermodule.controller.auth;

import com.usermodule.dto.auth.LoginRequestDTO;
import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.user.UserAddRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.auth.AuthService;
import com.usermodule.service.user.UserService;
import com.usermodule.utils.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final HttpUtil httpUtil;
    private final ApiResponseInspector apiResponseInspector;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        var loginResponseDTO = authService
                .login(loginRequestDTO, httpUtil.getHostName(),httpUtil.getHostAddress(), httpUtil.getRequestIp(request));
        return apiResponseInspector.apiResponseBuilder(loginResponseDTO, "");
    }

    @PostMapping("/logout")
    public ApiResponseDTO logout() {
        return null;
    }

    @PostMapping("/signup")
    public ApiResponseDTO signup(@Valid @RequestBody UserAddRequestDTO userAddRequestDTO, HttpServletRequest request) {
        var userResponseDTO = userService.add(userAddRequestDTO);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "");
    }
}
