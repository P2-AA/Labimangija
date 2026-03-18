package org.example.labimangija.hashgrader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logija {
    private static final Path LOG_DIR = Path.of(System.getProperty("user.dir"), "labimangud", "hashgrader");
    private final Path fail;

    public Logija() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            throw new IllegalStateException("Logikataloogi loomine ebaõnnestus", e);
        }
        fail = LOG_DIR.resolve(sdf.format(new Date()) + ".txt");
    }

    public void logi(String sisu) {
        try (BufferedWriter bw = Files.newBufferedWriter(
                fail,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(sisu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
