package com.oliver.siloker.component;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class FileUtils {

    public static Path validateFilename(String filename, String uploadDir) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(filename));
        if (originalFilename.contains(".."))
            throw new SecurityException("Filename contains invalid path sequence: " + originalFilename);

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        Path destinationFile = uploadPath.resolve(originalFilename).normalize();
        if (!destinationFile.startsWith(uploadPath))
            throw new SecurityException("Cannot store file outside the designated directory.");

        return uploadPath;
    }
}
