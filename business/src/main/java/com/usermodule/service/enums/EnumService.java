package com.usermodule.service.enums;

import com.usermodule.dto.enums.EnumResponseDTO;
import com.usermodule.model.user.UserStatus;
import com.usermodule.utils.LabeledEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnumService {


    public <T extends Enum<T> & LabeledEnum> List<EnumResponseDTO> getAllByType(Class<T>  enumClass){
        log.debug("EnumService.getAllByType started");
        List<EnumResponseDTO> enumResponseDTOList = new ArrayList<>();
        for (T enumConstant : enumClass.getEnumConstants()) {
            enumResponseDTOList.add(map(enumConstant));
        }
        return enumResponseDTOList;
    }

    public <T extends Enum<T> & LabeledEnum> EnumResponseDTO map(T enumType){
        log.debug("enumType.name {}", enumType.name());
        log.debug("enumType.label {}", enumType.getLabel());
        return EnumResponseDTO.builder()
                .value(enumType.name())
                .label(enumType.getLabel())
                .build();
    }
}
