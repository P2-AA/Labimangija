package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.common.KasutajaAndmed;
import ee.ut.labimangija.graphgrader.Graaf.Graaf;
import ee.ut.labimangija.graphgrader.Graaf.Kaar;
import ee.ut.labimangija.graphgrader.Graaf.Tipp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class Logija {

    public static void logi(List<String> vead, Graaf g, List<String> sammud, String algo, boolean kaarteKaalud, boolean tipuKaalud) {
        Path logiKaust = AppPaths.resolve("labimangud", "graphgrader", algo);
        String ajatempel = new SimpleDateFormat("ddMMyy-HHmmss.SSS").format(new Date());
        Path logiFail = logiKaust.resolve(algo + "_logi_" + ajatempel + ".txt");

        try {
            Files.createDirectories(logiKaust);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var bw = Files.newBufferedWriter(logiFail)) {
            bw.write(KasutajaAndmed.logiPais());
            bw.write(System.lineSeparator());
            bw.write("p edge %d %d%s%n".formatted(g.tipud.size(), g.tipud.stream().mapToInt(e -> e.alluvad.size()).sum(), (tipuKaalud ? tippudeKaalud(g) : "")));
            for (Tipp t : g.tipud)
                for (Kaar k : t.kaared)
                    bw.write("e " + t.tähis + " " + k.lopp.tähis + (kaarteKaalud ? (" " + k.kaal) : "") + "\n");

            bw.write("\n");
            int idx = 0;
            for (String s : sammud) {
                bw.write(s + "\n");
                if (s.endsWith("VIGA")) bw.write(vead.get(idx++) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String tippudeKaalud(Graaf g) {
        return String.join(" ", g.tipud.stream().map(e -> String.valueOf(e.kaal)).toList());
    }

}


