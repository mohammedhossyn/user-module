package com.usermodule.dto.user.forgetAndChangePassword;

import lombok.Builder;

@Builder
public record ForgetPasswordResponseDTO(String username,
                                        String code){}
