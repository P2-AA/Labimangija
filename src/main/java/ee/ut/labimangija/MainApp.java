package ee.ut.labimangija;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ee/ut/labimangija/main-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1120, 760);
        stage.setTitle("Läbimängija");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


