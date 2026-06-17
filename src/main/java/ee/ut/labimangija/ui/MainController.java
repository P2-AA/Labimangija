package ee.ut.labimangija.ui;

import java.io.IOException;
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
    private void initialize() throws IOException {
        loadView("home-view.fxml");
    }

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
        loadView("home-view.fxml");
    }

    @FXML
    private void handleAbout(ActionEvent event) throws IOException {
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
    private void handleGraphAlgorithms(ActionEvent event) throws IOException {
        loadViewFromResource("/ee/ut/labimangija/graphgrader/Programm.fxml");
        ensureWindowSize(1120, 760);
    }

    @FXML
    private void handleAlgorithmGrader(ActionEvent event) throws IOException {
        loadViewFromResource("/ee/ut/labimangija/treeheapgrader/hello-view.fxml");
        ensureWindowSize(1120, 760);
    }

    @FXML
    private void handleHashTables(ActionEvent event) throws IOException {
        loadViewFromResource("/ee/ut/labimangija/hashgrader/hashgrader-view.fxml");
        ensureWindowSize(1120, 760);
    }

    @FXML
    private void handleArrayAlgorithms(ActionEvent event) throws IOException {
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

    private void loadView(String fxmlName) throws IOException {
        loadViewFromResource("/ee/ut/labimangija/" + fxmlName);
    }

    private void loadViewFromResource(String resourcePath) throws IOException {
        URL resource = getClass().getResource(resourcePath);
        if (resource == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        Parent view = FXMLLoader.load(resource);
        asetaVaade(view);
    }

    private void asetaVaade(Parent view) {
        contentHolder.getChildren().setAll(view);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
    }
}
