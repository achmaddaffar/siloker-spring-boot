package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(ApiRoutes.DOWNLOAD)
@RequiredArgsConstructor
public class DownloadController {

    private final JobApplicationService jobApplicationService;

    @GetMapping("/cv/{filename:.+}")
    public ResponseEntity<Resource> downloadCv(
            @PathVariable String filename
    ) throws MalformedURLException, FileNotFoundException {
        Path filePath = Paths.get("/uploads/cv/").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists())
            throw new FileNotFoundException("CV not found: " + filename);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
