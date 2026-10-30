package com.odc.digilearn.security;

import com.odc.digilearn.modules.user.domain.Role;
import com.odc.digilearn.modules.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record AuthenticatedUserDetails(User user) implements UserDetails {
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(Role::name).map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
    }
    @Override public String getPassword() { return user.getPasswordHash(); }
    @Override public String getUsername() { return user.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return user.isEnabled(); }
}
