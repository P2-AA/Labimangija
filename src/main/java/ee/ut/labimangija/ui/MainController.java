package ee.ut.labimangija.ui;

import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private AnchorPane contentHolder;

    @FXML
    private VBox kulgriba;

    @FXML
    private VBox navAccordion;

    @FXML
    private void initialize() {
        loadView("home-view.fxml");
    }

    @FXML
    private void handleHome(ActionEvent event) {
        loadView("home-view.fxml");
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        loadView("about-view.fxml");
    }

    @FXML
    private void toggleKulgriba(ActionEvent event) {
        boolean nahtav = !navAccordion.isVisible();
        navAccordion.setVisible(nahtav);
        navAccordion.setManaged(nahtav);
    }

    @FXML
    private void handlePreferences(ActionEvent event) {
        AnchorPane placeholder = new AnchorPane();
        placeholder.setPrefSize(600, 400);
        Label silt = new Label("Kohahoidja");
        silt.setLayoutX(10);
        silt.setLayoutY(10);
        placeholder.getChildren().add(silt);
        asetaVaade(placeholder);
    }

    @FXML
    private void handleGraphAlgorithms(ActionEvent event) {
        loadViewFromResource("/ee/ut/labimangija/graphgrader/Programm.fxml");
        ensureWindowSize(1120, 760);
    }

    @FXML
    private void handleAlgorithmGrader(ActionEvent event) {
        loadViewFromResource("/ee/ut/labimangija/algorithmgrader/hello-view.fxml");
        ensureWindowSize(1120, 760);
    }

    @FXML
    private void handleHashTables(ActionEvent event) {
        loadViewFromResource("/ee/ut/labimangija/hashgrader/hashgrader-view.fxml");
        ensureWindowSize(1120, 760);
    }

    @FXML
    private void handleArrayAlgorithms(ActionEvent event) {
        loadViewFromResource("/ee/ut/labimangija/arraygrader/arraygrader-view.fxml");
        ensureWindowSize(1120, 760);
    }

    private void ensureWindowSize(double minWidth, double minHeight) {
        if (contentHolder.getScene() == null || !(contentHolder.getScene().getWindow() instanceof Stage stage)) {
            return;
        }

        if (stage.getWidth() < minWidth) {
            stage.setWidth(minWidth);
        }
        if (stage.getHeight() < minHeight) {
            stage.setHeight(minHeight);
        }
    }

    private void loadView(String fxmlName) {
        loadViewFromResource("/ee/ut/labimangija/" + fxmlName);
    }

    private void loadViewFromResource(String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource == null) {
                System.err.println("FXML faili ei leitud: " + resourcePath);
                kuvaLaadimisviga("FXML faili ei leitud: " + resourcePath.substring(resourcePath.lastIndexOf('/') + 1));
                return;
            }

            Parent view = FXMLLoader.load(resource);
            asetaVaade(view);
        } catch (Exception e) {
            e.printStackTrace();
            kuvaLaadimisviga("Vaate laadimine ebaõnnestus: " + resourcePath.substring(resourcePath.lastIndexOf('/') + 1));
        }
    }

    private void kuvaLaadimisviga(String sonum) {
        AnchorPane placeholder = new AnchorPane();
        placeholder.setPrefSize(600, 400);
        Label silt = new Label(sonum);
        silt.setWrapText(true);
        silt.setLayoutX(10);
        silt.setLayoutY(10);
        placeholder.getChildren().add(silt);
        asetaVaade(placeholder);
    }

    private void asetaVaade(Parent view) {
        contentHolder.getChildren().setAll(view);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
    }
}
