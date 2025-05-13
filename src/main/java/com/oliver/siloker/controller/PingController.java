package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.BASE_URL + "/ping")
public class PingController {

    @GetMapping
    public ResponseEntity<BaseResponse<String>> ping() {
        return ResponseEntity.ok(
                new BaseResponse<>(
                        200,
                        "PONG",
                        "PONG"
                )
        );
    }
}
