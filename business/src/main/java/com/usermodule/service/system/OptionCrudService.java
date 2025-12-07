package com.usermodule.service.system;

import com.usermodule.exception.BusinessException;
import com.usermodule.model.system.OptionEntity;
import com.usermodule.repository.system.OptionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionCrudService {

    private final OptionRepository optionRepository;

    public OptionEntity create(@NonNull OptionEntity optionEntity) {
        log.debug("OptionCrudService.create started");
        if (optionEntity.getOptionId() == null) {
            return optionRepository.save(optionEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public OptionEntity read(@NonNull Long id) {
        log.debug("OptionCrudService.read started");
        Optional<OptionEntity> optionalOption = optionRepository.findById(id);
        if (optionalOption.isPresent()) {
            return optionalOption.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public OptionEntity update(@NonNull OptionEntity optionEntity) {
        log.debug("OptionCrudService.update started");
        if (optionRepository.existsById(optionEntity.getOptionId())) {
            return optionRepository.save(optionEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull OptionEntity optionEntity) {
        log.debug("OptionCrudService.delete started");
        if (optionRepository.existsById(optionEntity.getOptionId())) {
            optionRepository.delete(optionEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public OptionEntity saveOrUpdate(@NonNull OptionEntity optionEntity) {
        log.debug("OptionCrudService.saveOrUpdate started");
        return optionRepository.save(optionEntity);
    }
}
