package com.usermodule.service.user;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.user.ForgetPasswordCodeEntity;
import com.usermodule.repository.user.ForgetPasswordCodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgetPasswordCodeCrudService {
    private final ForgetPasswordCodeRepository forgetPasswordCodeRepository;

    public ForgetPasswordCodeEntity create(@NonNull ForgetPasswordCodeEntity forgetPasswordCodeEntity) {
        log.debug("ForgetPasswordCodeCrudService.create started");
        if (forgetPasswordCodeEntity.getForgetPasswordCodeId() == null) {
            return forgetPasswordCodeRepository.save(forgetPasswordCodeEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public ForgetPasswordCodeEntity read(@NonNull Long id) {
        log.debug("ForgetPasswordCodeCrudService.read started");
        Optional<ForgetPasswordCodeEntity> optionalForgetPasswordCode = forgetPasswordCodeRepository.findById(id);
        if (optionalForgetPasswordCode.isPresent()) {
            return optionalForgetPasswordCode.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public ForgetPasswordCodeEntity update(@NonNull ForgetPasswordCodeEntity forgetPasswordCodeEntity) {
        log.debug("ForgetPasswordCodeCrudService.update started");
        if (forgetPasswordCodeRepository.existsById(forgetPasswordCodeEntity.getForgetPasswordCodeId())) {
            return forgetPasswordCodeRepository.save(forgetPasswordCodeEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull ForgetPasswordCodeEntity forgetPasswordCodeEntity) {
        log.debug("ForgetPasswordCodeCrudService.delete started");
        if (forgetPasswordCodeRepository.existsById(forgetPasswordCodeEntity.getForgetPasswordCodeId())) {
            forgetPasswordCodeRepository.delete(forgetPasswordCodeEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public ForgetPasswordCodeEntity saveOrUpdate(@NonNull ForgetPasswordCodeEntity forgetPasswordCodeEntity) {
        log.debug("ForgetPasswordCodeCrudService.saveOrUpdate started");
        return forgetPasswordCodeRepository.save(forgetPasswordCodeEntity);
    }
}
