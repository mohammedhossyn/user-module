package com.usermodule.dto.user;

import com.usermodule.dto.user.validation.Password;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserAddRequestDTO
        (String username,
         @NotEmpty
         @Size(min = 6, message = "validation.password")
         @Password
         String password,
         @Pattern(regexp = "^[0-9]{11}$", message = "validation.mobileNumber")
         @NotEmpty
         String mobileNumber,
         Long roleId,
         Set<Long> permissions) {
}

//         @NotEmpty
//         String firstName,
//         @NotEmpty
//         String surname,
//         @Size(min = 10, max = 10, message = "{validation.nationalCardId}")
//         @Pattern(regexp = "^\\d{10}$", message = "validation.nationalCardId")
//         @NotEmpty
//         String nationalCardId,
//         @Size(min = 11, max = 11, message = "{validation.mobileNumber}")
