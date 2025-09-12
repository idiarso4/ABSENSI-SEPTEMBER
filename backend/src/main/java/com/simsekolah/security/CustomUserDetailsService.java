package com.simsekolah.security;

import com.simsekolah.entity.Permission;
import com.simsekolah.entity.Role;
import com.simsekolah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetailsService implementation for loading user-specific data
 * This service integrates with the User entity and Role system
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Try by email first
            com.simsekolah.entity.User user = userRepository.findByEmail(username)
                    .orElseGet(() -> userRepository.findByUsername(username)
                            .orElseGet(() -> userRepository.findByNip(username)
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username))));

            if (!Boolean.TRUE.equals(user.getIsActive())) {
                throw new UsernameNotFoundException("User account is disabled: " + username);
            }

            return createUserDetails(user.getEmail(), user.getPassword(), getUserAuthorities(user));
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("User lookup error for: " + username + ", reason: " + e.getMessage());
        }
    }

    /**
     * Create UserDetails object from user information
     */
    private UserDetails createUserDetails(String username, String password, Collection<String> authorities) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        return User.builder()
                .username(username)
                .password(password)
                .authorities(grantedAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Get user authorities from user entity
     */
    private Collection<String> getUserAuthorities(com.simsekolah.entity.User user) {
        List<String> authorities = new ArrayList<>();
        
        // Add user type as role
        authorities.add("ROLE_" + user.getUserType().name());
        
        // Add base user role
        authorities.add("ROLE_USER");
        
        // Add specific roles from user's role assignments
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                authorities.add("ROLE_" + role.getName().toUpperCase());
                
                // Add permissions from roles
                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        authorities.add(permission.getName().toUpperCase());
                    }
                }
            }
        }
        
        return authorities;
    }
}
