package com.usermodule.controller.system;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.system.SettingRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.system.SettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/setting")
public class SettingController {

    private final SettingService settingService;
    private final ApiResponseInspector apiResponseInspector;

    @GetMapping("/get/{id}")
    public ApiResponseDTO getSetting(@PathVariable ("id") Long id) {
        var setting = settingService.findById(id);
        return apiResponseInspector.apiResponseBuilder(setting, "",
                true);
    }

    @PostMapping("/update")
    public ApiResponseDTO updateSetting(@Valid @RequestBody SettingRequestDTO settingRequestDTO) {
        var setting = settingService.update(settingRequestDTO);
        return apiResponseInspector.apiResponseBuilder(setting, "",
                true);
    }
}
