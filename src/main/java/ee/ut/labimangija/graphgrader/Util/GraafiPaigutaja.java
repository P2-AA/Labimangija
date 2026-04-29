package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.graphgrader.Graaf.Tipp;
import ee.ut.labimangija.graphgrader.Graaf.TippGraafil;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.List;

public final class GraafiPaigutaja {
    private static final double PADDING = 35.0;

    private GraafiPaigutaja() {
    }

    public static void paigutaRingina(List<Tipp> tipud, Pane ala) {
        if (tipud == null || tipud.isEmpty()) {
            return;
        }

        double laius = ala.getWidth() > 0 ? ala.getWidth() : (ala.getPrefWidth() > 0 ? ala.getPrefWidth() : 800.0);
        double korgus = ala.getHeight() > 0 ? ala.getHeight() : (ala.getPrefHeight() > 0 ? ala.getPrefHeight() : 600.0);

        double keskX = laius / 2.0;
        double keskY = korgus / 2.0;
        double raadiusX = Math.max(80.0, laius / 2.0 - PADDING - 30.0);
        double raadiusY = Math.max(80.0, korgus / 2.0 - PADDING - 30.0);

        if (tipud.size() == 1) {
            Tipp tipp = tipud.get(0);
            tipp.x = (int) Math.round(keskX);
            tipp.y = (int) Math.round(keskY);
            return;
        }

        for (int i = 0; i < tipud.size(); i++) {
            double nurk = -Math.PI / 2.0 + (2.0 * Math.PI * i) / tipud.size();
            Tipp tipp = tipud.get(i);
            tipp.x = (int) Math.round(keskX + Math.cos(nurk) * raadiusX);
            tipp.y = (int) Math.round(keskY + Math.sin(nurk) * raadiusY);
        }
    }

    public static void lisaLiigutamine(TippGraafil tipp, Text tekst, Pane ala, Runnable uuenda) {
        tekst.setX(tipp.getCenterX() - 3);
        tekst.setY(tipp.getCenterY() + 3);

        tipp.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double uusX = piiridesse(e.getX(), PADDING, ala.getWidth() - PADDING);
            double uusY = piiridesse(e.getY(), PADDING, ala.getHeight() - PADDING);

            tipp.setCenterX(uusX);
            tipp.setCenterY(uusY);
            tipp.tipp.x = (int) Math.round(uusX);
            tipp.tipp.y = (int) Math.round(uusY);
            tekst.setX(uusX - 3);
            tekst.setY(uusY + 3);
            uuenda.run();
        });
    }

    private static double piiridesse(double vaartus, double min, double max) {
        if (max < min) {
            return min;
        }
        return Math.max(min, Math.min(max, vaartus));
    }
}
