package ee.ut.labimangija.arraygrader.kasutajaliides;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.SisendiAllikaDialoog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArraySisendiValija {
    private static final Path SISENDI_JUUR = AppPaths.resolve("sisendid", "massiivialgoritmid");
    private static final Pattern TAISARV = Pattern.compile("-?\\d+");

    public record Sisend(int[] massiiv, String kirjeldus) {
    }

    public static Sisend valiSisend(ArrayGraderEngine.Algoritm algoritm) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva("Massiivi sisend");
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) {
            return null;
        }
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) {
            return ArraySisendiGenereerija.genereeriFail(algoritm);
        }
        return valiFail(algoritm);
    }

    static Path sisendiKaust(ArrayGraderEngine.Algoritm algoritm) {
        return SISENDI_JUUR.resolve(switch (algoritm) {
            case MULLIMEETOD -> "mullimeetod";
            case PISTEMEETOD -> "pistemeetod";
            case VALIKUMEETOD -> "valikumeetod";
            case VALIKU_KIIRMEETOD -> "valiku_kiirmeetod";
        });
    }

    private static Sisend valiFail(ArrayGraderEngine.Algoritm algoritm) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali massiivialgoritmi sisendfail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        Path kaust = sisendiKaust(algoritm);
        if (Files.isDirectory(kaust)) {
            chooser.setInitialDirectory(kaust.toFile());
        }

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) {
            return null;
        }
        return loeFail(valitud.toPath());
    }

    private static Sisend loeFail(Path fail) {
        try {
            List<String> read = Files.readAllLines(fail, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(rida -> !rida.isEmpty())
                    .toList();
            if (read.isEmpty()) {
                naitaViga("Sisendfail on tühi.");
                return null;
            }
            int[] massiiv = parsiMassiiv(read.get(0));
            if (massiiv.length < 5) {
                naitaViga("Sisendfailis peab olema vähemalt viis arvu.");
                return null;
            }
            return new Sisend(massiiv, "Fail: " + fail.toAbsolutePath());
        } catch (IOException e) {
            naitaViga("Sisendfaili lugemine ebaõnnestus: " + e.getMessage());
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

    private static void naitaViga(String teade) {
        new Alert(Alert.AlertType.ERROR, teade, ButtonType.OK).showAndWait();
    }
}
