package com.usermodule.dto.user;

import com.usermodule.model.user.UserEntity;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Builder
public record UserDetailsDTO(UserEntity user, Long id) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        user.getUserRolePermissions().stream()
                .flatMap(urp -> urp.getPermissions().stream())
                .filter(permission -> permission.getCode() != null)
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .forEach(authorities::add);
        return authorities;
    }



    public UserEntity getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long id() {
        return user.getUserId();
    }

}
