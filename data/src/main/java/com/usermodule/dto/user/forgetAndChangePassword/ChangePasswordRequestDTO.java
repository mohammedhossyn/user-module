package com.usermodule.dto.user.forgetAndChangePassword;

import com.usermodule.dto.user.validation.Password;

public record ChangePasswordRequestDTO (String username,
                                       String code,
                                       @Password
                                       String newPassword){
}
