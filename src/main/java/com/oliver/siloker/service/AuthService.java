package com.oliver.siloker.service;

import com.oliver.siloker.component.FileUtils;
import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.UserRepository;
import com.oliver.siloker.model.request.LoginRequest;
import com.oliver.siloker.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final String uploadDir = "/uploads/images/";

    public User registerUser(
            RegisterRequest request
    ) throws IOException {
        if (userRepository.findByPhoneNumber(request.getPhone_number()).isPresent())
            throw new IllegalArgumentException("Phone number is already used");

        User user = new User();
        user.setPhoneNumber(request.getPhone_number());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFull_name());
        user.setBio(request.getBio());

        Path uploadPath = FileUtils.validateFilename(request.getProfile_picture().getOriginalFilename(), uploadDir);
        String filename = UUID.randomUUID() + "_" + request.getProfile_picture().getOriginalFilename();
        File destination = new File(uploadPath.toFile(), filename);
        try {
            request.getProfile_picture().transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save profile picture");
        }
        String profilePictureUrl = "/images/" + filename;
        user.setProfilePictureUrl(profilePictureUrl);

        return userRepository.save(user);
    }

    public String loginUser(
            LoginRequest request
    ) throws ResourceNotFoundException {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User is not found"));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getPhoneNumber(),
                request.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);

        return jwtUtils.generateToken(user);
    }
}
