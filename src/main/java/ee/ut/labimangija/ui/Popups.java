package ee.ut.labimangija.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class Popups {

    public static void showInstructions(String text) {
        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Juhend");
        dialog.setContentText(text);

        dialog.getDialogPane().setPrefWidth(420);
        dialog.getDialogPane()
                .getScene()
                .getWindow()
                .setOnCloseRequest(e -> dialog.hide());
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("info.jpg"));

        dialog.showAndWait();
    }

    /**
     * Info popup. This should be used for errors that are recoverable and not penalized.
     */
    public static void showInfo(String text) {
        showInfo(text, null);
    }

    /**
     * Info popup. This should be used for errors that are recoverable and not penalized.
     */
    public static void showInfo(String text, String details) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
        alert.setTitle("Teavitus");
        alert.setHeaderText(text);
        alert.setContentText(details);
        alert.showAndWait();
    }

    // TODO: Should we have separate popups for unrecoverable and penalized errors respectively?

    /**
     * Error popup. This should be used for errors that are unrecoverable or penalized.
     */
    public static void showError(String text) {
        showError(text, null);
    }

    /**
     * Error popup. This should be used for errors that are unrecoverable or penalized.
     */
    public static void showError(String text, String details) {
        Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle("Viga");
        alert.setHeaderText(text);
        alert.setContentText(details);
        alert.showAndWait();
    }

}
