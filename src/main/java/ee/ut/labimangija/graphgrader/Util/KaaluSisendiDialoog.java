package ee.ut.labimangija.graphgrader.Util;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public final class KaaluSisendiDialoog {
    private KaaluSisendiDialoog() {
    }

    public static Optional<String> kuva() {
        return kuva(null);
    }

    public static Optional<String> kuva(String lisainfo) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sisend");
        String pohi = "Mis on kaare lõpptipu kaugus?";
        if (lisainfo == null || lisainfo.isBlank()) {
            dialog.setHeaderText(pohi);
        } else {
            dialog.setHeaderText(pohi + "\n" + lisainfo);
        }
        dialog.setContentText("");
        return dialog.showAndWait().map(String::trim);
    }
}
