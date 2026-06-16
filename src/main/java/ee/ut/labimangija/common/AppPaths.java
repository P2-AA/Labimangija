package ee.ut.labimangija.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class AppPaths {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private AppPaths() {
    }

    public static Path root() {
        Path workingDirectory = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path parent = workingDirectory.getParent();
        if (parent != null
                && "bin".equalsIgnoreCase(workingDirectory.getFileName().toString())
                && Files.isDirectory(parent.resolve("lib"))) {
            return parent;
        }
        return workingDirectory;
    }

    public static Path resolve(String first, String... more) {
        Path path = Path.of(first, more);
        return path.isAbsolute() ? path : root().resolve(path).normalize();
    }

    public static String logFile(String algorithm) {
        return normalize(algorithm) + "_" + TIMESTAMP_FORMATTER.format(LocalDateTime.now()) + ".txt";
    }

    public static String generatedFile() {
        return "gen_" + TIMESTAMP_FORMATTER.format(LocalDateTime.now()) + ".txt";
    }

    private static String normalize(String value) {
        return value
                .toLowerCase()
                .replace(' ', '_');
    }
}

