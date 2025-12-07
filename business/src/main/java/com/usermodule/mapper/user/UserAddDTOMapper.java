package com.usermodule.mapper.user;

import com.usermodule.dto.user.UserAddRequestDTO;
import com.usermodule.model.user.UserEntity;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.usermodule.model.user.UserStatus.ACTIVE;

@Component
public class UserAddDTOMapper implements Function<UserAddRequestDTO, UserEntity> {

    @Override
    public UserEntity apply(@NonNull UserAddRequestDTO userAddRequestDTO) {
                return UserEntity.builder()
                .username(userAddRequestDTO.username())
                .password(userAddRequestDTO.password())
                .mobileNumber(userAddRequestDTO.mobileNumber())
                .status(ACTIVE)
                .build();
    }

    public UserEntity applyCustomer(String username, String mobileNumber) {
        String password = RandomStringUtils.randomAlphanumeric(8);
        return UserEntity.builder()
                .username(username)
                .password(password)
                .mobileNumber(mobileNumber)
                .status(ACTIVE)
                .build();
    }


}
