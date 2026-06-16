package ee.ut.labimangija.algorithmgrader.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.common.KasutajaAndmed;

// Klassi implementatsioon põhineb peamiselt Markus Michelise loodud lahendusel.
// Eeskujuks kasutatud töö: "kahendpuu- ja kuhjaalgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/e07a9cf2-900d-4db8-9d05-5c24d48e424c
public class Logija {
    private static final Path LOGI_KAUST = AppPaths.resolve("labimangud", "kahendpuud_kuhjad");

    public static void logiViga(List<String> vead, String algoritm) {
        if (vead == null || vead.isEmpty()) {
            return;
        }

        try {
            Files.createDirectories(LOGI_KAUST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path logiPath = LOGI_KAUST.resolve(AppPaths.logFile(algoritm));

        try (BufferedWriter bw = Files.newBufferedWriter(
                logiPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.CREATE_NEW
        )) {
            bw.append(KasutajaAndmed.logiPais()).append("\n");
            bw.append(vead.get(0)).append("\n\n");
            for (String viga : vead.subList(1, vead.size())) {
                bw.append(viga).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

