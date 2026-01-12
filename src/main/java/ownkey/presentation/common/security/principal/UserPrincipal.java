package ownkey.presentation.common.security.principal;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record UserPrincipal(Long userId, String role) implements UserDetails {

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role == null
                ? Collections.emptyList()
                : List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() { return null; }

    @Override
    @NonNull
    public String getUsername() { return String.valueOf(userId); }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}