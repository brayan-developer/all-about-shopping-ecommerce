package com.microservice_auth.service;


import com.microservice_auth.entity.User;
import com.microservice_auth.exceptions.ResourceNotFoundException;
import com.microservice_auth.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow( () -> new ResourceNotFoundException("User", "email", email));

        return new org.springframework.security.core.userdetails
                .User(user.getEmail(),
                user.getPassword(), getAuthorities(user));

    }


    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return new org.springframework.security.core.userdetails
                .User(user.getEmail(),
                user.getPassword(), getAuthorities(user));
    }


    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority(String.valueOf(user.getRole().getName())));
    }
}
