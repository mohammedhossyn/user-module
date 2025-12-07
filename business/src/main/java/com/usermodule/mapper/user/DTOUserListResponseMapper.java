package com.usermodule.mapper.user;

import com.usermodule.dto.user.UserSearchRequestDTO;
import com.usermodule.model.user.UserEntity;
import com.usermodule.model.user.UserStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DTOUserListResponseMapper implements Function<UserSearchRequestDTO , UserEntity> {

    @Override
    public UserEntity apply(@NonNull  UserSearchRequestDTO userSearchRequestDTO) {
        UserStatus userStatus = !userSearchRequestDTO.status().isEmpty()
                ? UserStatus.valueOf(userSearchRequestDTO.status()) : null;
        return UserEntity.builder()
                .username(userSearchRequestDTO.username())
                .statusForSearch(userSearchRequestDTO.status())
                .mobileNumber(userSearchRequestDTO.mobileNumber())
                .status(userStatus)
                .build();
    }
}