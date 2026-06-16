package ee.ut.labimangija.arraygrader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.common.KasutajaAndmed;

public class ArrayGraderLogija {
    private static final Path LOG_DIR = AppPaths.resolve("labimangud", "massiivid");

    private final Path fail;
    private boolean paisKirjutatud;

    public ArrayGraderLogija(String algoritm) {
        String failinimi = AppPaths.logFile(algoritm);

        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            throw new UncheckedIOException("Logikataloogi loomine ebaõnnestus", e);
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
            if (!paisKirjutatud) {
                pw.println(KasutajaAndmed.logiPais());
                pw.println();
                paisKirjutatud = true;
            }
            pw.println(sisu);
        } catch (IOException e) {
            throw new UncheckedIOException("Logimine ebaõnnestus", e);
        }
    }

    private String normaliseeri(String vaartus) {
        return vaartus
                .toLowerCase()
                .replace(' ', '_');
    }
}

