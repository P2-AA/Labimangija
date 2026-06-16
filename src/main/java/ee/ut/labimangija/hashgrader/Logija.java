package ee.ut.labimangija.hashgrader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.common.KasutajaAndmed;

// Klassi implementatsioon põhineb peamiselt Karolin Konradi loodud lahendusel.
// Eeskujuks kasutatud töö: "Paisktabelialgoritmide läbimängu automaatse hindaja loomine", kättesaadav aadressil:
// https://thesis.cs.ut.ee/de177ee2-57a8-428a-b4b9-cac9982f5bd4

public class Logija {
    private static final Path LOG_DIR = AppPaths.resolve("labimangud", "paisktabelid");
    private final Path fail;
    private boolean paisKirjutatud;

    public Logija(String algoritm) {
        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            throw new IllegalStateException("Paisktabelite logikataloogi loomine ebaõnnestus", e);
        }
        fail = LOG_DIR.resolve(AppPaths.logFile(algoritm));
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
            throw new RuntimeException("Paisktabelite logimine ebaõnnestus", e);
        }
    }
}

