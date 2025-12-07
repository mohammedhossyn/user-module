package com.usermodule.service.system;

import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.system.GeneralHistoryRequestDTO;
import com.usermodule.dto.system.GeneralHistoryResponseDTO;
import com.usermodule.model.system.GeneralHistoryEntity;
import com.usermodule.repository.system.GeneralHistoryRepository;
import com.usermodule.utils.DateUtil;
import com.usermodule.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeneralHistoryService {

    private final GeneralHistoryRepository generalHistoryRepository;
    private final DateUtil dateUtil;
    private final PaginationUtil<GeneralHistoryResponseDTO> generalHistoryResponseDTOPaginationUtil;
    private final GeneralHistoryCrudService generalHistoryCrudService;

    public PageResponseWithHeaderDTO<GeneralHistoryResponseDTO>search
            (Pageable pageable, GeneralHistoryRequestDTO generalHistoryRequestDTO) {
        log.debug("GeneralHistoryService.search started");
        Page<GeneralHistoryResponseDTO> pages = generalHistoryRepository.findGeneralChangeHistoryEntities(pageable,
                        generalHistoryRequestDTO.changedTable(), generalHistoryRequestDTO.tableId(), null)
                .map(history -> GeneralHistoryResponseDTO.builder().tableField(history.getTableField())
                        .changeOrigin(history.getChangeOrigin()).changedBy(history.getChangedBy())
                        .changeDate(history.getChangeDate()).oldValue(history.getOldValue()).newValue(history.getNewValue()).build());
        log.debug("GeneralHistoryService.search ended");
        return generalHistoryResponseDTOPaginationUtil.getHeader(pages, GeneralHistoryResponseDTO.class);
    }

    public void generateHistory(Object oldEntity, Object newEntity, Long tablePk, String changedTable, Integer cityCode,
                                Integer changeOrigin, Date changeDate) {
        log.debug("GeneralHistoryService.generateHistory started");
        Map<String, Object> map1 = generateFieldsMap(oldEntity);
        Map<String, Object> map2 = generateFieldsMap(newEntity);

        Set<String> list1 = map1.keySet();
        Set<String> list2 = map2.keySet();

        Set<String> fields = new HashSet<>();
        fields.addAll(list1);
        fields.addAll(list2);

        for (String fieldName : fields) {
            Object object1 = map1.get(fieldName);
            Object object2 = map2.get(fieldName);

            if (object1 == null) {
                object1 = "";
            }
            if (object2 == null) {
                object2 = "";
            }
            if (ObjectUtils.notEqual(object1, object2)) {
                GeneralHistoryEntity generalHistoryEntity = new GeneralHistoryEntity();
                generalHistoryEntity.setChangedTable(changedTable);
                generalHistoryEntity.setChangeOrigin(changeOrigin);
                generalHistoryEntity.setChangeDate(changeDate);
                generalHistoryEntity.setChangedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                generalHistoryEntity.setTableId(tablePk);
                generalHistoryEntity.setTableField(fieldName);
                generalHistoryEntity.setOldValue(object1.toString());
                generalHistoryEntity.setNewValue(object2.toString());

                log.debug("GeneralHistoryService.generateHistory ended");
                generalHistoryCrudService.create(generalHistoryEntity);
            }
        }

    }

    @SneakyThrows
    public Map<String, Object> generateFieldsMap(Object object) {
        log.debug("GeneralHistoryService.generateFieldsMap started");
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getModifiers() == Modifier.PRIVATE && !field.getName().contains("Entity") &&
                    !field.getName().contains("Entities") && !field.getName().equals("version") &&
                    !field.getName().contains("createdDate") && !field.getName().equals("createdBy") &&
                    !field.getName().contains("lastModifiedBy") && !field.getName().equals("actionDate") &&
                    !field.getName().equals("modifiedDate")) {
                field.setAccessible(true);
                Object o = field.get(object);
                if (o != null) {
                    fillMap(map, field.getName(), field.getType().getName(), o);
                }
            }
        }
        log.debug("GeneralHistoryService.generateFieldsMap ended");
        return map;
    }

    private void fillMap(Map<String, Object> map, String name, String type, Object field) {
        log.debug("GeneralHistoryService.fillMap started");
        if (field == null) {
            field = " ";
        } else if (type.equals("java.util.Date")) {
            field = dateUtil.getSolarDateByFormat((Date) field);
        }
        map.put(name, field);
    }
}
