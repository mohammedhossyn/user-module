package com.usermodule.service.system;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.system.SettingEntity;
import com.usermodule.repository.system.SettingRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettingCrudService {
    
    private final SettingRepository settingRepository;

    public SettingEntity create(@NonNull SettingEntity settingEntity) {
        log.debug("SettingCrudService.create started");
        if (settingEntity.getSettingId() == null) {
            return settingRepository.save(settingEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public SettingEntity read(@NonNull Long id) {
        log.debug("SettingCrudService.read started");
        Optional<SettingEntity> optionalSetting = settingRepository.findById(id);
        if (optionalSetting.isPresent()) {
            return optionalSetting.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public SettingEntity update(@NonNull SettingEntity settingEntity) {
        log.debug("SettingCrudService.update started");
        if (settingRepository.existsById(settingEntity.getSettingId())) {
            return settingRepository.save(settingEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull SettingEntity settingEntity) {
        log.debug("SettingCrudService.delete started");
        if (settingRepository.existsById(settingEntity.getSettingId())) {
            settingRepository.delete(settingEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public SettingEntity saveOrUpdate(@NonNull SettingEntity settingEntity) {
        log.debug("SettingCrudService.saveOrUpdate started");
        return settingRepository.save(settingEntity);
    }

    public SettingEntity findById(Long id) {
        log.debug("SettingCrudService.findById started");
        return settingRepository.findById(id).orElse(null);
    }
}
