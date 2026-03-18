package org.example.labimangija.hashgrader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ResourceReader {
    private ResourceReader() {
    }

    public static List<String> readLines(String path) throws IOException {
        Path filePath = Path.of(System.getProperty("user.dir"), path);
        if (Files.exists(filePath)) {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        }

        try (InputStream inputStream = ResourceReader.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Sisendfaili ei leitud: " + path);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).lines().toList();
        }
    }
}
