package ee.ut.labimangija.ui;

import ee.ut.labimangija.common.AppPaths;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class SisendiAllikaDialoog {
    private static final String HEADER = "Vali, kas sisend genereerida või lugeda failist.";

    private SisendiAllikaDialoog() {
    }

    public static Valik kuva() {
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setTitle("Sisend");
        dialog.setHeaderText(HEADER);

        ButtonType genereeri = new ButtonType("Genereeri");
        ButtonType valiFail = new ButtonType("Vali fail");
        ButtonType katkesta = new ButtonType("Katkesta", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(genereeri, valiFail, katkesta);

        Optional<ButtonType> valik = dialog.showAndWait();
        if (valik.isEmpty() || valik.get() == katkesta) {
            return Valik.KATKESTA;
        }
        if (valik.get() == genereeri) {
            return Valik.GENEREERI;
        }
        return Valik.VALI_FAIL;
    }

    public static String valiFail(Path kaust) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali sisendfail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        if (Files.isDirectory(kaust)) {
            chooser.setInitialDirectory(kaust.toFile());
        } else {
            chooser.setInitialDirectory(AppPaths.root().toFile());
        }

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) return null;
        return valitud.getAbsolutePath();
    }

    public enum Valik {
        GENEREERI,
        VALI_FAIL,
        KATKESTA
    }
}
