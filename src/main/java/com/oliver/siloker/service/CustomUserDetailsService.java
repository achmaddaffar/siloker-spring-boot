package com.oliver.siloker.service;

import com.oliver.siloker.model.custom.CustomUserDetails;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByPhoneNumber(username)
                .map(this::mapFromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found!"));
    }

    private CustomUserDetails mapFromEntity(User user) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(user.getPhoneNumber());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setId(user.getId());
        customUserDetails.setName(user.getFullName());

        return customUserDetails;
    }
}
