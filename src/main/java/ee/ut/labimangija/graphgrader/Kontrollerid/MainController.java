package ee.ut.labimangija.graphgrader.Kontrollerid;

import ee.ut.labimangija.common.Juhendid;
import ee.ut.labimangija.ui.Popups;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

public class MainController {
    private static final double JUHEND_NUPU_X = 95.0;
    private static final double JUHEND_NUPU_Y = 5.0;

    public Tab Laiuti;
    public Tab Sygavuti1;
    public Tab Sygavuti2;
    public Tab Prim;
    public Tab Kruskal;
    public Tab Dijkstra;
    public Tab FW;
    public Tab BF;
    public Tab Kahn;
    public Tab Eeldus;

    @FXML
    public void initialize() {
        lisaJuhendiNupp(Laiuti, Juhendid.graaf("laiuti"));
        lisaJuhendiNupp(Sygavuti1, Juhendid.graaf("sygavuti_ees"));
        lisaJuhendiNupp(Sygavuti2, Juhendid.graaf("sygavuti_lopp"));
        lisaJuhendiNupp(Prim, Juhendid.graaf("prim"));
        lisaJuhendiNupp(Kruskal, Juhendid.graaf("kruskal"));
        lisaJuhendiNupp(Dijkstra, Juhendid.graaf("dijkstra"));
        lisaJuhendiNupp(FW, Juhendid.graaf("fw"));
        lisaJuhendiNupp(BF, Juhendid.graaf("bf"));
        lisaJuhendiNupp(Kahn, Juhendid.graaf("kahn"));
        lisaJuhendiNupp(Eeldus, Juhendid.graaf("eeldus"));
    }

    private void lisaJuhendiNupp(Tab tab, String info) {
        if (!(tab.getContent() instanceof Pane pane)) {
            return;
        }

        Button juhendNupp = new Button("Juhend");
        juhendNupp.setLayoutX(JUHEND_NUPU_X);
        juhendNupp.setLayoutY(JUHEND_NUPU_Y);
        juhendNupp.setOnAction(e -> Popups.showInstructions(info));
        pane.getChildren().add(juhendNupp);
        juhendNupp.toFront();
    }
}
