package com.usermodule.service.system;


import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.system.OptionRequestDTO;
import com.usermodule.dto.system.OptionResponseDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.model.system.GeneralChangeHistoryEntity;
import com.usermodule.model.system.OptionEntity;
import com.usermodule.repository.system.OptionRepository;
import com.usermodule.utils.PaginationUtil;
import com.usermodule.utils.TransactionUtil;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionService {

    private static final Map<String, String> optionMap = new HashMap<>();
    private final OptionRepository optionRepository;
    private final PaginationUtil<OptionResponseDTO> optionResponseDTOPaginationUtil;
    private final OptionCrudService optionCrudService;
    private final GeneralHistoryService generalHistoryService;
    private final TransactionUtil transactionUtil;

    @PostConstruct
    public void fillOptionMap() {
        log.debug("OptionService.fillOptionMap started");
        if (optionMap.isEmpty()) {
            List<OptionEntity> list = optionRepository.findAll();
            for (OptionEntity optionEntity : list) {
                optionMap.put(optionEntity.getCode(), optionEntity.getValue());
            }
        }
    }

    public String getStringValueByCode(String code) {
        log.debug("OptionService.getStringValueByCode started");
        return optionMap.get(code);
    }

    public Integer getIntegerValueByCode(String code) {
        log.debug("OptionService.getIntegerValueByCode started");
        var value = optionMap.get(code);
        if (value != null) {
            return Integer.valueOf(value);
        } else {
            return null;
        }
    }

    public void reloadOptionMap() {
        log.debug("OptionService.reloadOptionMap started");
        optionMap.clear();
        fillOptionMap();
    }
    public PageResponseWithHeaderDTO<OptionResponseDTO> search(@NonNull Pageable pageable,
                                                                     OptionRequestDTO OptionRequestDTO) {
        log.debug("OptionService.search started");
        Page<OptionResponseDTO> pages = optionRepository.findOptionEntity(pageable,
                OptionRequestDTO.description(),
                OptionRequestDTO.code(),
                OptionRequestDTO.value()).map(optionEntity ->
                OptionResponseDTO.builder()
                        .code(optionEntity.getCode())
                        .value(optionEntity.getValue())
                        .description(optionEntity.getDescription())
                        .optionId(optionEntity.getOptionId())
                        .build());
        return optionResponseDTOPaginationUtil.getHeader(pages, OptionResponseDTO.class);
    }
    public OptionResponseDTO update(@NonNull Long id, OptionRequestDTO OptionRequestDTO) {
        log.debug("OptionService.update started");
        OptionEntity optionEntity;
        try {
            OptionEntity oldOptionEntity = optionCrudService.read(id);
            transactionUtil.openTransaction();
            optionEntity = optionCrudService.update(OptionEntity.builder()
                    .code(OptionRequestDTO.code())
                    .value(OptionRequestDTO.value())
                    .description(OptionRequestDTO.description())
                    .optionId(id)
                    .lastModifiedDate(new Date())
                    .build());
            generalHistoryService.generateHistory(oldOptionEntity, optionEntity,
                    oldOptionEntity.getOptionId(), "OPTION",
                    null, GeneralChangeHistoryEntity.CHANGE_ORIGIN_OPTION, new Date());
            reloadOptionMap();
            transactionUtil.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transactionUtil.rollback();
            throw new BusinessException(e);
        }
        log.debug("OptionService.update ended");
        return OptionResponseDTO.builder()
                .code(optionEntity.getCode())
                .value(optionEntity.getValue())
                .description(optionEntity.getDescription())
                .optionId(optionEntity.getOptionId())
                .build();
    }
}