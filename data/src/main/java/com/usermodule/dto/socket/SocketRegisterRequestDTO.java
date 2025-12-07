package com.usermodule.dto.socket;


import lombok.Builder;

@Builder
public record SocketRegisterRequestDTO(Long loginUserId) {
}
