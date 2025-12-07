package com.usermodule.service.system;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.system.GeneralHistoryEntity;
import com.usermodule.repository.system.GeneralHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralHistoryCrudService {

    private final GeneralHistoryRepository generalHistoryRepository;

    public GeneralHistoryEntity create(@NonNull GeneralHistoryEntity generalHistoryEntity) {
        log.debug("GeneralHistoryCrudService.create started");
        if (generalHistoryEntity.getGeneralChangeHistoryId() == null) {
            return generalHistoryRepository.save(generalHistoryEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public GeneralHistoryEntity read(@NonNull Long id) {
        log.debug("GeneralHistoryCrudService.read started");
        Optional<GeneralHistoryEntity> optionalGeneralHistory = generalHistoryRepository.findById(id);
        if (optionalGeneralHistory.isPresent()) {
            return optionalGeneralHistory.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public GeneralHistoryEntity update(@NonNull GeneralHistoryEntity generalHistoryEntity) {
        log.debug("GeneralHistoryCrudService.update started");
        if (generalHistoryRepository.existsById(generalHistoryEntity.getGeneralChangeHistoryId())) {
            return generalHistoryRepository.save(generalHistoryEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull GeneralHistoryEntity generalHistoryEntity) {
        log.debug("GeneralHistoryCrudService.delete started");
        if (generalHistoryRepository.existsById(generalHistoryEntity.getGeneralChangeHistoryId())) {
            generalHistoryRepository.delete(generalHistoryEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public GeneralHistoryEntity saveOrUpdate(@NonNull GeneralHistoryEntity generalHistoryEntity) {
        log.debug("GeneralHistoryCrudService.saveOrUpdate started");
        return generalHistoryRepository.save(generalHistoryEntity);
    }
}
