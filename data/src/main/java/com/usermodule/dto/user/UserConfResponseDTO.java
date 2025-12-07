package com.usermodule.dto.user;

import lombok.Builder;

import java.util.List;

@Builder
public record UserConfResponseDTO(Long id,
                                  List<String> permissions,
                                  Integer unseenNotifyCount,
                                  Object theme,
                                  Object fav) {
}
