package org.example.labimangija.arraygrader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArrayGraderLogija {
    private static final Path LOG_DIR = Path.of(System.getProperty("user.dir"), "labimangud", "arraygrader");

    private final Path fail;

    public ArrayGraderLogija(String algoritm, String labimang) {
        String ajatempel = new SimpleDateFormat("ddMMyy-HHmmss.SSS").format(new Date());
        String failinimi = normaliseeri(algoritm) + "_" + normaliseeri(labimang) + "_" + ajatempel + ".txt";

        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            throw new IllegalStateException("Arraygraderi logikataloogi loomine ebaõnnestus", e);
        }

        fail = LOG_DIR.resolve(failinimi);
    }

    public Path getFail() {
        return fail;
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
            throw new RuntimeException("Arraygraderi logimine ebaõnnestus", e);
        }
    }

    private String normaliseeri(String vaartus) {
        return vaartus
                .toLowerCase()
                .replace(' ', '_');
    }
}
