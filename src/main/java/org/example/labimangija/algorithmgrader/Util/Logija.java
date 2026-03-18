package org.example.labimangija.algorithmgrader.Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Logija {
    private static final Path LOGI_KAUST = Path.of(System.getProperty("user.dir"), "labimangud", "kahendpuu_kuhi");

    public static void logiViga(List<String> vead, String logiFail) {
        if (vead == null || vead.isEmpty()) {
            return;
        }

        try {
            Files.createDirectories(LOGI_KAUST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path logiPath = LOGI_KAUST.resolve(logiFail);
        try (BufferedWriter bw = Files.newBufferedWriter(
                logiPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            bw.append(vead.get(0)).append("\n\n");
            for (String viga : vead.subList(1, vead.size())) {
                bw.append(viga).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logiViga(String viga, String logiFail) {
        logiViga(List.of(viga), logiFail + ".txt");
    }
}
