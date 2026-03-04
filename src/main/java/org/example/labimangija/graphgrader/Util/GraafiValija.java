package org.example.labimangija.graphgrader.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GraafiValija {
    public static String valiSuvaline(String kaust) {
        Path kaustaTee = Path.of(kaust);
        if (!Files.isDirectory(kaustaTee)) return null;
        try {
            List<Path> p = Files.list(kaustaTee).filter(Files::isRegularFile).toList();
            if (p.isEmpty()) return null;
            Random r = new Random();
            return p.get(r.nextInt(p.size())).toString();
        } catch (Exception ignored) {}
        return null;
    }

    public static String valiFailVoiGenereeri(String kaust, GraafiGenereerija.Tyyp tyyp) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Graafi allikas");
        dialog.setHeaderText("Vali, kas sisend lugeda failist või genereerida.");

        ButtonType genereeri = new ButtonType("Genereeri");
        ButtonType valiFail = new ButtonType("Vali fail");
        ButtonType katkesta = new ButtonType("Katkesta", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(genereeri, valiFail, katkesta);

        Optional<ButtonType> valik = dialog.showAndWait();
        if (valik.isEmpty() || valik.get() == katkesta) return null;
        if (valik.get() == genereeri) return GraafiGenereerija.genereeriFail(tyyp, kaust);
        return valiFail(kaust);
    }

    private static String valiFail(String kaust) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali graafifail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        Path kaustaTee = Path.of(kaust);
        if (Files.isDirectory(kaustaTee)) chooser.setInitialDirectory(kaustaTee.toFile());

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) return null;
        return valitud.getAbsolutePath();
    }
}

