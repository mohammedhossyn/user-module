package com.usermodule.mapper.system;

import com.usermodule.dto.system.SettingResponseDTO;
import com.usermodule.model.system.SettingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class SettingResponseDTOMapper implements Function<SettingEntity, SettingResponseDTO> {
    @Override
    public SettingResponseDTO apply(SettingEntity settingEntity) {
        return SettingResponseDTO.builder()
                .id(settingEntity.getSettingId())
                .build();
    }
}
