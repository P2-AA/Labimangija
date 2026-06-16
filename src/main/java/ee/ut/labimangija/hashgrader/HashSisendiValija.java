package ee.ut.labimangija.hashgrader;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.SisendiAllikaDialoog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class HashSisendiValija {
    private static final Path SISENDI_JUUR = AppPaths.resolve("sisendid", "paisktabelid");

    public static String valiSisend(String tyyp) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva();
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) return null;
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) return HashSisendiGenereerija.genereeriFail(tyyp);
        return SisendiAllikaDialoog.valiFail(sisendiKaust(tyyp));
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

    static void naitaViga(String teade) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
        alert.setTitle("Viga");
        alert.setHeaderText("Viga");

        Label sisu = new Label(teade);
        sisu.setWrapText(true);
        alert.getDialogPane().setContent(sisu);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setPrefWidth(560);

        alert.showAndWait();
    }
}
