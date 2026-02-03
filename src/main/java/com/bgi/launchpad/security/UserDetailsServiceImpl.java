package com.bgi.launchpad.security;

import com.bgi.launchpad.model.User;
import com.bgi.launchpad.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetailsService implementation for Spring Security.
 * Loads user data from database and converts to Spring Security UserDetails.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with email: " + email));

        // Check if user is active
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User account is deactivated");
        }

        // Convert to Spring Security UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getIsActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                getAuthorities(user)
        );
    }

    /**
     * Convert user role to Spring Security authorities
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add role with ROLE_ prefix (required by Spring Security)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        
        return authorities;
    }

    /**
     * Load user entity by email (used in services)
     */
    @Transactional(readOnly = true)
    public User loadUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with email: " + email));
    }
}