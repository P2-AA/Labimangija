package org.example.labimangija.algorithmgrader;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AlgorithmGrader extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/labimangija/algorithmgrader/hello-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        stage.setTitle("Kahendpuu- ja kuhjaalgoritmide läbimängija");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
