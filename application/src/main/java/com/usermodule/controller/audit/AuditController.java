package com.usermodule.controller.audit;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.system.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;
    private final ApiResponseInspector apiResponseInspector;

    @GetMapping("/search/{entity}/{referenceId}")
//    @PreAuthorize("hasAuthority('AUDIT_MANAGEMENT') and hasAuthority('AUDIT_LIST') and #username == principal.username")
    public ApiResponseDTO search(@PathVariable String entity,
                                 @PathVariable Long referenceId) {
        var auditResponseDTO = auditService.search(entity, referenceId);
        return apiResponseInspector.apiResponseBuilder(auditResponseDTO, "");
    }
}
