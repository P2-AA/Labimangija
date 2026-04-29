package ee.ut.labimangija.ui;

import ee.ut.labimangija.common.KasutajaAndmed;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HomeController {
    @FXML
    private TextField nimiField;

    @FXML
    private TextField matrikkelField;

    @FXML
    private void initialize() {
        if (nimiField == null || matrikkelField == null) {
            return;
        }

        nimiField.setText(KasutajaAndmed.getNimi());
        matrikkelField.setText(KasutajaAndmed.getMatrikkel());

        nimiField.textProperty().addListener((ignored, vana, uus) -> KasutajaAndmed.setNimi(uus));
        matrikkelField.textProperty().addListener((ignored, vana, uus) -> KasutajaAndmed.setMatrikkel(uus));
    }
}
