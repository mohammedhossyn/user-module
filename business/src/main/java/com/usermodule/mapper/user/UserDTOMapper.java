package com.usermodule.mapper.user;

import com.usermodule.dto.user.UserResponseDTO;
import com.usermodule.mapper.user.permission.PermissionDTOMapper;
import com.usermodule.mapper.user.role.RoleDTOMapper;
import com.usermodule.model.user.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserDTOMapper implements Function<UserEntity, UserResponseDTO> {

    private final PermissionDTOMapper permissionDTOMapper;
    private final RoleDTOMapper roleDTOMapper;

    @Override
    public UserResponseDTO apply(@NonNull UserEntity userEntity) {
        return UserResponseDTO.builder()
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .status(userEntity.getStatus().getLabel())
                .mobileNumber(userEntity.getMobileNumber())
                .build();
    }
}
