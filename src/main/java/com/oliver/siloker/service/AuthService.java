package com.oliver.siloker.service;

import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.repository.EmployerRepository;
import com.oliver.siloker.model.repository.JobSeekerRepository;
import com.oliver.siloker.model.repository.UserRepository;
import com.oliver.siloker.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    public User registerUser(
            RegisterRequest request
    ) {
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent())
            throw new IllegalArgumentException("Phone number is already used");

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setBio(request.getBio());

        return userRepository.save(user);
    }
}
