package ee.ut.labimangija;

import ee.ut.labimangija.ui.Popups;
import javafx.application.Application;
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

        Popups.showError("Programmi jooksutamisel visati ootamatu erind", throwable.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}


