package com.oliver.siloker.component;

import org.springframework.stereotype.Component;

@Component
public class ApiRoutes {

    public static final String BASE_URL = "/api/v1";
    public static final String AUTH = BASE_URL + "/auth";
    public static final String JOB = BASE_URL + "/job";
    public static final String USER = BASE_URL + "/user";
    public static final String DOWNLOAD = BASE_URL + "/download";
}
