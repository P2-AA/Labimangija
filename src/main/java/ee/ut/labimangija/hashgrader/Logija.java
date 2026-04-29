package ee.ut.labimangija.hashgrader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.common.KasutajaAndmed;

public class Logija {
    private static final Path LOG_DIR = AppPaths.resolve("labimangud", "hashgrader");
    private final Path fail;
    private boolean paisKirjutatud;

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
            if (!paisKirjutatud) {
                pw.println(KasutajaAndmed.logiPais());
                pw.println();
                paisKirjutatud = true;
            }
            pw.println(sisu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

