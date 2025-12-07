package com.usermodule.service.user;

import com.usermodule.dto.user.UserResponseDTO;
import lombok.NonNull;

public interface IUserService {

    UserResponseDTO getUser(@NonNull Long id);
}
