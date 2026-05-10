package ee.ut.labimangija.algorithmgrader.Controllers;

import ee.ut.labimangija.common.Juhendid;
import ee.ut.labimangija.graphgrader.Util.Teavitaja;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class AlgorithmMainController {
    private static final double JUHEND_NUPU_X = 375.0;
    private static final double JUHEND_NUPU_Y = 70.0;
    

    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            lisaJuhendiNupp(tabPane.getTabs().get(i), juhendiVoti(i));
        }
    }

    private void lisaJuhendiNupp(Tab tab, String guideKey) {
        if (!(tab.getContent() instanceof Pane pane)) {
            return;
        }

        Button juhendNupp = new Button("Juhend");
        juhendNupp.setLayoutX(JUHEND_NUPU_X);
        juhendNupp.setLayoutY(JUHEND_NUPU_Y);
        juhendNupp.setOnAction(e -> Teavitaja.teavita(Juhendid.kahendpuu(guideKey), "Juhend"));
        pane.getChildren().add(juhendNupp);
        juhendNupp.toFront();
    }

    private String juhendiVoti(int tabIndex) {
        return switch (tabIndex) {
            case 0 -> "jarjend_bst";
            case 1 -> "eemaldamine_bst";
            case 2 -> "lisamine_avl";
            case 3 -> "eemaldamine_avl";
            case 4 -> "jarjendi_kuhjastamine";
            case 5 -> "kuhjameetod";
            default -> "";
        };
    }
}
