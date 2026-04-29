package ee.ut.labimangija.hashgrader;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.SisendiAllikaDialoog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class HashSisendiValija {
    private static final Path SISENDI_JUUR = AppPaths.resolve("sisendid", "paisktabelid");

    public static String valiSisend(String tyyp) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva("Paisktabeli sisend");
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) {
            return null;
        }
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) {
            return HashSisendiGenereerija.genereeriFail(tyyp);
        }
        return valiFail(tyyp);
    }

    static Path sisendiKaust(String tyyp) {
        return SISENDI_JUUR.resolve(switch (tyyp) {
            case "l" -> "lisamine";
            case "e" -> "eemaldamine";
            case "k" -> "kimbumeetod";
            case "p" -> "positsioonimeetod";
            default -> throw new IllegalArgumentException("Tundmatu paisktabeli ülesande tüüp: " + tyyp);
        });
    }

    private static String valiFail(String tyyp) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali paisktabeli sisendfail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        Path kaust = sisendiKaust(tyyp);
        if (Files.isDirectory(kaust)) {
            chooser.setInitialDirectory(kaust.toFile());
        }

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) {
            return null;
        }
        return valitud.getAbsolutePath();
    }

    static void naitaViga(String teade) {
        new Alert(Alert.AlertType.ERROR, teade, ButtonType.OK).showAndWait();
    }
}
