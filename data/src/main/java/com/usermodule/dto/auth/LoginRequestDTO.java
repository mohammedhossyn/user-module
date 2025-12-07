package com.usermodule.dto.auth;

import com.usermodule.dto.user.validation.Password;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO
        (@NotEmpty String username,
//         @NotEmpty @Size(min = 1, message = "{validation.password}")
         @Password
         String password) {
}
