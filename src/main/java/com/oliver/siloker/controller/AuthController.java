package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.request.LoginRequest;
import com.oliver.siloker.model.request.RegisterRequest;
import com.oliver.siloker.model.response.BaseResponse;
import com.oliver.siloker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Boolean>> register(
            @RequestBody RegisterRequest request
    ) {
        User user = authService.registerUser(request);
        if (user == null)
            return ResponseEntity
                    .status(400)
                    .body(new BaseResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Failed",
                            false
                    ));

        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.CREATED.value(),
                        "Success",
                        true
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(
            @RequestBody LoginRequest request
    ) throws ResourceNotFoundException {
        String token = authService.loginUser(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        token
                )
        );
    }
}
