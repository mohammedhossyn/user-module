package com.usermodule.dto.event;


import com.usermodule.model.user.UserEntity;
import lombok.Builder;

@Builder
public record EventAddRequestDTO (Long referenceId,
                                 UserEntity user,
                                 String type,
                                  String message) {
}
