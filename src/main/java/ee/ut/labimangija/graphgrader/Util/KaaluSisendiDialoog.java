package ee.ut.labimangija.graphgrader.Util;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public final class KaaluSisendiDialoog {
    private KaaluSisendiDialoog() {
    }

    public static Optional<String> kuva() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sisend");
        dialog.setHeaderText("Mis on tippude vaheline kaal?");
        dialog.setContentText("");
        return dialog.showAndWait().map(String::trim);
    }
}
