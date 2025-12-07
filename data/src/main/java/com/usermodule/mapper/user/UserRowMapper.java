package com.usermodule.mapper.user;

import com.usermodule.model.user.UserEntity;
import com.usermodule.model.user.UserStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<UserEntity> {
    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        return UserEntity.builder()
                .userId(rs.getLong("USER_ID"))
                .username(rs.getString("USER_NAME"))
                .password(rs.getString("PASSWORD"))
                .mobileNumber(rs.getString("MOBILE_NUMBER"))
                .status(UserStatus.valueOf(rs.getString("STATUS")))
                .build();
    }
}