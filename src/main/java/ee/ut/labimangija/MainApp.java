package ee.ut.labimangija;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.lang.reflect.InvocationTargetException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(MainApp::onUncaughtException);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ee/ut/labimangija/main-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1120, 760);
        stage.setTitle("Läbimängija");
        stage.setScene(scene);
        stage.show();
    }

    private static void onUncaughtException(Thread thread, Throwable throwable) {
        while (throwable.getCause() != null &&
                (throwable instanceof InvocationTargetException || throwable.getCause() instanceof InvocationTargetException)) {
            throwable = throwable.getCause();
        }

        throwable.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Viga");
        alert.setHeaderText("Programmi jooksutamisel visati ootamatu erind");
        alert.setContentText(throwable.toString());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


