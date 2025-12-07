package com.usermodule.service.system;

import com.usermodule.dto.system.SettingRequestDTO;
import com.usermodule.dto.system.SettingResponseDTO;
import com.usermodule.mapper.system.SettingResponseDTOMapper;
import com.usermodule.model.system.SettingEntity;
import com.usermodule.repository.system.SettingRepository;
import com.usermodule.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;
    private final UserService userService;
    private final SettingResponseDTOMapper settingResponseDTOMapper;
    private final SettingCrudService settingCrudService;

    public SettingEntity findById(Long id) {
        log.debug("SettingService.findById started");
        return settingRepository.findById(id).orElseThrow();
    }


    public SettingResponseDTO update(SettingRequestDTO settingRequestDTO) {
        var setting = settingCrudService.findById(settingRequestDTO.id());
        return settingResponseDTOMapper.apply(settingRepository.save(setting));
    }
}
