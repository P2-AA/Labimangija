package ee.ut.labimangija.common;

import java.nio.file.Files;
import java.nio.file.Path;

public final class AppPaths {
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
}

