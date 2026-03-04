package org.example.labimangija;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    private AnchorPane contentHolder;

    @FXML
    private void initialize() {
        // Lae esialgne vaade (Pealeht)
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
    private void handlePreferences(ActionEvent event) {
        // Kasutab kohahoidjat, kui ei ole eraldi vaadet loodud
        AnchorPane placeholder = new AnchorPane();
        placeholder.setPrefSize(600, 400);
        javafx.scene.control.Label l = new javafx.scene.control.Label("Kohahoidja");
        l.setLayoutX(10);
        l.setLayoutY(10);
        placeholder.getChildren().add(l);
        contentHolder.getChildren().setAll(placeholder);
        AnchorPane.setTopAnchor(placeholder, 0.0);
        AnchorPane.setBottomAnchor(placeholder, 0.0);
        AnchorPane.setLeftAnchor(placeholder, 0.0);
        AnchorPane.setRightAnchor(placeholder, 0.0);
    }

    @FXML
    private void handleGraphAlgorithms(ActionEvent event) {
        loadViewFromResource("/org/example/labimangija/graphgrader/Programm.fxml");
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
        try {
            URL resource = getClass().getResource(fxmlName);
            if (resource == null) {
                System.err.println("FXML faili ei leitud: " + fxmlName);
                return;
            }
            javafx.scene.Parent view = FXMLLoader.load(resource);
            contentHolder.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadViewFromResource(String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource == null) {
                System.err.println("FXML faili ei leitud: " + resourcePath);
                return;
            }
            javafx.scene.Parent view = FXMLLoader.load(resource);
            contentHolder.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

