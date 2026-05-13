package ee.ut.labimangija.graphgrader.Util;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class Teavitaja {

    public static void teavita(String s, String h) {
        Dialog<Void> dialog = new Dialog<>();
        String tekst = s == null ? "" : s;
        double wrapLaius = 420;

        Label sisu = new Label(tekst);
        sisu.setWrapText(true);
        sisu.setPrefWidth(wrapLaius);
        sisu.setMinWidth(Region.USE_PREF_SIZE);
        sisu.setMinHeight(Region.USE_PREF_SIZE);
        dialog.getDialogPane().setContent(sisu);

        Text mootja = new Text(tekst);
        mootja.setWrappingWidth(wrapLaius);
        double tekstiKorgus = Math.ceil(mootja.getLayoutBounds().getHeight());
        double paneeliLaius = wrapLaius + 40;
        double paneeliKorgus = tekstiKorgus + 110;
        dialog.getDialogPane().setPrefSize(paneeliLaius, paneeliKorgus);
        dialog.setResizable(false);

        dialog.setTitle(h);
        dialog.getDialogPane()
                .getScene()
                .getWindow()
                .setOnCloseRequest(e -> {
                    dialog.hide();
                });
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                h.equals("Viga") ? "err.jpg" : "info.jpg"));
        dialog.showAndWait();
    }
}
