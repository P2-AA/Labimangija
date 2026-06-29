package ee.ut.labimangija.arraygrader.kasutajaliides;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.Popups;
import ee.ut.labimangija.ui.SisendiAllikaDialoog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArraySisendiValija {
    private static final Path SISENDI_JUUR = AppPaths.resolve("sisendid", "massiivid");
    private static final Pattern TAISARV = Pattern.compile("-?\\d+");

    public record Sisend(int[] massiiv, String kirjeldus) {
    }

    public static Sisend valiSisend(ArrayGraderEngine.Algoritm algoritm) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva();
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) return null;
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) return ArraySisendiGenereerija.genereeriFail(algoritm);
        String fail = SisendiAllikaDialoog.valiFail(sisendiKaust(algoritm));
        if (fail == null) return null;
        return loeFail(Path.of(fail));
    }

    static Path sisendiKaust(ArrayGraderEngine.Algoritm algoritm) {
        return SISENDI_JUUR.resolve(algoritm.name().toLowerCase());
    }

    private static Sisend loeFail(Path fail) {
        try {
            List<String> read = Files.readAllLines(fail, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(rida -> !rida.isEmpty())
                    .toList();
            if (read.isEmpty()) {
                Popups.showError("Sisendfail on tühi.");
                return null;
            }
            int[] massiiv = parsiMassiiv(read.get(0));
            if (massiiv.length < 5) {
                Popups.showError("Sisendfailis peab olema vähemalt viis arvu.");
                return null;
            }
            return new Sisend(massiiv, "Fail: " + fail.toAbsolutePath());
        } catch (IOException e) {
            Popups.showError("Sisendfaili lugemine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    private static int[] parsiMassiiv(String rida) {
        Matcher matcher = TAISARV.matcher(rida);
        return matcher.results()
                .map(match -> match.group())
                .mapToInt(Integer::parseInt)
                .toArray();
    }

}
