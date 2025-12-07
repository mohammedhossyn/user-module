package com.usermodule.utils;

import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private static final ThreadLocal<UserRecord> User = new ThreadLocal<>();

    public static UserRecord GET_USER() {
        return User.get();
    }

    public void setUser(Long userId, String username, String hostName, String hostAddress, String requestIp) {
        User.set(UserRecord.builder()
                .userId(userId)
                .username(username)
                .hostName(hostName)
                .hostAddress(hostAddress)
                .requestIp(requestIp)
                .build());
    }

    @Builder
    public record UserRecord(Long userId, String username, String hostName, String hostAddress, String requestIp) {
    }
}
