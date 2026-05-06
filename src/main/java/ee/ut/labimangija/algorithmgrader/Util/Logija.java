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


public class Logija {
    private static final Path LOGI_KAUST = AppPaths.resolve("labimangud", "kahendpuu_kuhi");

    public static void logiViga(List<String> vead, String logiFail) {
        if (vead == null || vead.isEmpty()) {
            return;
        }

        try {
            Files.createDirectories(LOGI_KAUST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String ajatempel = new SimpleDateFormat("ddMMyy-HHmmss.SSS").format(new Date());
        String failinimi = eemaldaTxt(logiFail) + "_" + ajatempel + ".txt";
        Path logiPath = LOGI_KAUST.resolve(failinimi);

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

    public static void logiViga(String viga, String logiFail) {
        logiViga(List.of(viga), logiFail + ".txt");
    }

    private static String eemaldaTxt(String nimi) {
        return nimi.endsWith(".txt") ? nimi.substring(0, nimi.length() - 4) : nimi;
    }
}

