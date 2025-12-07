package com.usermodule.service.user.permission;

import com.usermodule.dto.user.permission.PermissionResponseDTO;
import com.usermodule.dto.user.permission.PermissionTreeResponseDTO;
import com.usermodule.dto.user.validation.PermissionData;
import com.usermodule.mapper.user.permission.PermissionDTOMapper;
import com.usermodule.repository.user.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionMakeTreeService {

    private final PermissionRepository permissionRepository;
    private final PermissionDTOMapper permissionDTOMapper;

    public List<PermissionData> generatePermissionTree(List<PermissionTreeResponseDTO> data) {
        log.debug("PermissionMakeTreeService.generatePermissionTree started");
        List<PermissionData> menuMap = new ArrayList<>();
        List<PermissionData> newList = convertPermissionDataToTreeDTO(data);
        mergePermission(newList, menuMap);
        return menuMap;
    }

    private List<PermissionData> convertPermissionDataToTreeDTO(List<PermissionTreeResponseDTO> data) {
        log.debug("PermissionMakeTreeService.convertPermissionDataToTreeDTO started");
        List<PermissionData> newList = new ArrayList<>();
        for (PermissionTreeResponseDTO permissionTreeResponseDTO : data) {
            List<PermissionData> children = null;
            if (permissionTreeResponseDTO.children() != null) {
                children = convertPermissionDataToTreeDTO(permissionTreeResponseDTO.children());
            }
            newList.add(PermissionData.builder().permissionId(permissionTreeResponseDTO.permissionId())
                    .code(permissionTreeResponseDTO.code()).description(permissionTreeResponseDTO.description())
                    .children(children).build());
        }
        log.debug("PermissionMakeTreeService.convertPermissionDataToTreeDTO ended");
        return newList;
    }

    private PermissionData mergePermission(List<PermissionData> data, List<PermissionData> menuMap) {
        log.debug("PermissionMakeTreeService.mergePermission started");
        for (PermissionData item : data) {
            boolean existedItem = false;
            if (menuMap != null) {
                for (PermissionData permissionData : menuMap) {
                    if (permissionData.getCode().equals(item.getCode())) {
                        if (item.getChildren() != null) {
                            PermissionData child = mergePermission(item.getChildren(), permissionData.getChildren());
                            if (child != null) {
                                List<PermissionData> children = new ArrayList<>();
                                children.add(child);
                                permissionData.setChildren(children);
                            }
                        }
                        existedItem = true;
                        break;
                    }
                }
            } else {
                return item;
            }
            if (!existedItem) {
                menuMap.add(item);
            }
        }
        log.debug("PermissionMakeTreeService.mergePermission ended");
        return null;
    }

    private PermissionTreeResponseDTO makePermissionListWithLinkedList(LinkedList<PermissionResponseDTO> link,
                                                                       PermissionTreeResponseDTO parent) {
        log.debug("PermissionMakeTreeService.makePermissionListWithLinkedList started");
        PermissionTreeResponseDTO action = getLastPermissionData(link);
        List<PermissionTreeResponseDTO> childSet;
        if (link.isEmpty()) {
            if (parent != null) {
                childSet = new ArrayList<>();
                childSet.add(action);
                if (parent.children() != null && !parent.children().isEmpty()) {
                    PermissionTreeResponseDTO child = parent.children().get(0).withChild(childSet);
                    childSet = new ArrayList<>();
                    childSet.add(child);
                }
                return parent.withChild(childSet);
            } else {
                return action;
            }
        } else {
            if (parent == null) {
                return makePermissionListWithLinkedList(link, action);
            } else {
                childSet = new ArrayList<>();
                childSet.add(action);
                return makePermissionListWithLinkedList(link, parent.withChild(childSet));
            }
        }
    }

    private PermissionTreeResponseDTO getLastPermissionData(LinkedList<PermissionResponseDTO> link) {
        log.debug("PermissionMakeTreeService.getLastPermissionData started");
        PermissionResponseDTO parent = link.getLast();
        link.removeLast();
        return new PermissionTreeResponseDTO(parent.permissionId(), parent.code(), parent.description(), null);
    }

    public List<PermissionData> findAllPermissionTree() {
        log.debug("PermissionMakeTreeService.findAllPermissionTree started");
        var list = permissionRepository.findAll().stream().map(permissionDTOMapper).toList();

        List<PermissionTreeResponseDTO> actionDataList = new ArrayList<>();

        LinkedList<PermissionResponseDTO> link;
        for (PermissionResponseDTO permissionResponseDTO : list) {
            link = new LinkedList<>();
            if (permissionResponseDTO.permissionParent() == null) {
                link.add(permissionResponseDTO);
            } else {
                link.add(permissionResponseDTO);
                if (permissionResponseDTO.permissionParent().permissionParent() == null) {
                    link.add(permissionResponseDTO.permissionParent());
                } else {
                    link.add(permissionResponseDTO.permissionParent());
                    link.add(permissionResponseDTO.permissionParent().permissionParent());
                }
            }

            PermissionTreeResponseDTO permissionTreeResponseDTO = makePermissionListWithLinkedList(link, null);
            actionDataList.add(permissionTreeResponseDTO);
        }

        log.debug("PermissionMakeTreeService.findAllPermissionTree ended");
        return generatePermissionTree(actionDataList);
    }


}
