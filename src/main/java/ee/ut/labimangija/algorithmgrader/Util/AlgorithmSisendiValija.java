package ee.ut.labimangija.algorithmgrader.Util;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.SisendiAllikaDialoog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AlgorithmSisendiValija {
    private static final Path SISENDI_KAUST = AppPaths.resolve("sisendid", "kahendpuud_kuhjad");

    private AlgorithmSisendiValija() {
    }

    public enum Tyyp {
        BST_LISAMINE("Kahendotsimispuu lisamise sisend"),
        BST_EEMALDAMINE("Kahendotsimispuu eemaldamise sisend"),
        AVL_LISAMINE("AVL lisamise sisend"),
        AVL_EEMALDAMINE("AVL eemaldamise sisend"),
        KUHJASTAMINE("Kuhjastamise sisend"),
        KUHJAMEETOD("Kuhjameetodi sisend");

        private final String pealkiri;

        Tyyp(String pealkiri) {
            this.pealkiri = pealkiri;
        }

        public String pealkiri() {
            return pealkiri;
        }
    }

    public static String valiSisend(Tyyp tyyp) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva(tyyp.pealkiri());
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) {
            return null;
        }
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) {
            return AlgorithmSisendiGenereerija.genereeriFail(tyyp);
        }
        return valiFail(tyyp);
    }

    static Path sisendiKaust(Tyyp tyyp) {
        return SISENDI_KAUST.resolve(tyyp.name().toLowerCase());
    }

    private static String valiFail(Tyyp tyyp) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali sisendfail: " + tyyp.pealkiri().toLowerCase());
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
