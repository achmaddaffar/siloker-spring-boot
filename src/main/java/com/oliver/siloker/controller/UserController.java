package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.model.entity.user.Employer;
import com.oliver.siloker.model.entity.user.JobSeeker;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.request.RegisterEmployerRequest;
import com.oliver.siloker.model.request.RegisterJobSeekerRequest;
import com.oliver.siloker.model.request.UpdateUserRequest;
import com.oliver.siloker.model.response.BaseResponse;
import com.oliver.siloker.model.response.GetUserResponse;
import com.oliver.siloker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiRoutes.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse<GetUserResponse>> getUser() throws ResourceNotFoundException {
        User user = userService.getUser();
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        new GetUserResponse(
                                user.getId(),
                                user.getFullName(),
                                user.getPhoneNumber(),
                                user.getPassword(),
                                user.getBio(),
                                user.getEmployer() == null ? null : user.getEmployer().getId(),
                                user.getJobSeeker() == null ? null : user.getJobSeeker().getId(),
                                user.getProfilePictureUrl()
                        )
                )
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BaseResponse<Boolean>> updateUser(
            @RequestBody UpdateUserRequest request
    ) throws ResourceNotFoundException {
        User user = userService.updateUser(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        user != null
                )
        );
    }

    @PostMapping(
            path = "/update/profile_picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BaseResponse<Boolean>> uploadProfilePicture(
            MultipartFile profile_picture
    ) throws IOException, ResourceNotFoundException {
        Boolean isUploaded = userService.uploadProfilePicture(profile_picture);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        isUploaded
                )
        );
    }

    @PostMapping("/register-job-seeker")
    public ResponseEntity<BaseResponse<Boolean>> registerJobSeeker(
            @RequestBody RegisterJobSeekerRequest request
    ) throws ResourceNotFoundException {
        JobSeeker jobSeeker = userService.registerJobSeeker(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        jobSeeker != null
                )
        );
    }

    @PostMapping("/register-employer")
    public ResponseEntity<BaseResponse<Boolean>> registerEmployer(
            @RequestBody RegisterEmployerRequest request
    ) throws ResourceNotFoundException {
        Employer employer = userService.registerEmployer(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        employer != null
                )
        );
    }
}
