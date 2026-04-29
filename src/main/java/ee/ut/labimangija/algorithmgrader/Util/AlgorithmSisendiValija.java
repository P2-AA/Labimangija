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
    private static final Path SISENDI_KAUST = AppPaths.resolve("sisendid", "kahendpuu_kuhi");

    private AlgorithmSisendiValija() {
    }

    public enum Tyyp {
        BST_LISAMINE("Kahendotsimispuu lisamise sisend", "bst_lisamine"),
        BST_EEMALDAMINE("Kahendotsimispuu eemaldamise sisend", "bst_eemaldamine"),
        AVL_LISAMINE("AVL lisamise sisend", "avl_lisamine"),
        AVL_EEMALDAMINE("AVL eemaldamise sisend", "avl_eemaldamine"),
        KUHJASTAMINE("Kuhjastamise sisend", "kuhjastamine"),
        KUHJAMEETOD("Kuhjameetodi sisend", "kuhjameetod");

        private final String pealkiri;
        private final String failiPrefix;

        Tyyp(String pealkiri, String failiPrefix) {
            this.pealkiri = pealkiri;
            this.failiPrefix = failiPrefix;
        }

        public String pealkiri() {
            return pealkiri;
        }

        public String failiPrefix() {
            return failiPrefix;
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

    static Path sisendiKaust() {
        return SISENDI_KAUST;
    }

    private static String valiFail(Tyyp tyyp) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali sisendfail: " + tyyp.pealkiri().toLowerCase());
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        if (Files.isDirectory(SISENDI_KAUST)) {
            chooser.setInitialDirectory(SISENDI_KAUST.toFile());
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
