package com.usermodule.service.system;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.system.ErrorEntity;
import com.usermodule.repository.system.ErrorRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorCrudService {

    private final ErrorRepository errorRepository;

    public ErrorEntity create(@NonNull ErrorEntity errorEntity) {
        log.debug("ErrorCrudService.create started");
        if (errorEntity.getErrorId() == null) {
            return errorRepository.save(errorEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public ErrorEntity read(@NonNull Long id) {
        log.debug("ErrorCrudService.read started");
        Optional<ErrorEntity> optionalError = errorRepository.findById(id);
        if (optionalError.isPresent()) {
            return optionalError.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public ErrorEntity update(@NonNull ErrorEntity errorEntity) {
        log.debug("ErrorCrudService.update started");
        if (errorRepository.existsById(errorEntity.getErrorId())) {
            return errorRepository.save(errorEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull ErrorEntity errorEntity) {
        log.debug("ErrorCrudService.delete started");
        if (errorRepository.existsById(errorEntity.getErrorId())) {
            errorRepository.delete(errorEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public ErrorEntity saveOrUpdate(@NonNull ErrorEntity errorEntity) {
        log.debug("ErrorCrudService.saveOrUpdate started");
        return errorRepository.save(errorEntity);
    }
}
