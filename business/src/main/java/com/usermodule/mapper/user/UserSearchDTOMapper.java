package com.usermodule.mapper.user;

import com.usermodule.dto.user.UserSearchResponseDTO;
import com.usermodule.model.user.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserSearchDTOMapper implements Function<UserEntity, UserSearchResponseDTO> {

    @Override
    public UserSearchResponseDTO apply(@NonNull UserEntity userEntity) {
        return UserSearchResponseDTO.builder()
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .status(userEntity.getStatus().getLabel())
                .build();
    }
}
