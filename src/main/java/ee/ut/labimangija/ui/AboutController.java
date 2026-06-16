package ee.ut.labimangija.ui;

import ee.ut.labimangija.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AboutController {

    @FXML
    private Label applicationName;

    @FXML
    private void initialize() {
        String version = MainApp.class.getModule().getDescriptor().rawVersion().orElse("?");
        applicationName.setText("Läbimängija v" + version);
    }

}
