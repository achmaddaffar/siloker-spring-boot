package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.request.LoginRequest;
import com.oliver.siloker.model.request.RegisterRequest;
import com.oliver.siloker.model.response.BaseResponse;
import com.oliver.siloker.model.response.LoginResponse;
import com.oliver.siloker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(ApiRoutes.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BaseResponse<Boolean>> register(
            @ModelAttribute RegisterRequest request
    ) throws IOException {
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
    public ResponseEntity<BaseResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ) throws ResourceNotFoundException {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        response
                )
        );
    }
}
