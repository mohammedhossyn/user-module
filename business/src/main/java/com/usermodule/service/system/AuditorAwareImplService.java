package com.usermodule.service.system;

import com.usermodule.dto.user.UserDetailsDTO;
import com.usermodule.model.user.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditorAwareImplService implements AuditorAware<UserEntity> {

    @Override
    public @NonNull Optional<UserEntity> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetailsDTO) {
            return Optional.of(((UserDetailsDTO) authentication.getPrincipal()).getUser());
        } else {
            return Optional.empty();
        }
    }
}