package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.graphgrader.Graaf.Arrow;
import ee.ut.labimangija.graphgrader.Graaf.Kaar;
import javafx.scene.text.Text;

public final class KaareKaaluKuvaja {

    private KaareKaaluKuvaja() {
    }

    public static boolean peaksKuvama(Kaar kaar) {
        Kaar vastaskaar = leiaVastaskaar(kaar);
        if (vastaskaar == null || vastaskaar.kaal != kaar.kaal) {
            return true;
        }
        return kaar.algus.tähis.compareTo(kaar.lopp.tähis) < 0;
    }

    public static Text looKaaluTekst(Arrow arrow, Kaar kaar, String tekst) {
        if (!onVordseKaalugaKahepoolne(kaar)) {
            return new Text(arrow.midX, arrow.midY, tekst);
        }

        double keskX = (kaar.algus.tippGraafil.getCenterX() + kaar.lopp.tippGraafil.getCenterX()) / 2.0;
        double keskY = (kaar.algus.tippGraafil.getCenterY() + kaar.lopp.tippGraafil.getCenterY()) / 2.0;
        return new Text(keskX, keskY, tekst);
    }

    private static boolean onVordseKaalugaKahepoolne(Kaar kaar) {
        Kaar vastaskaar = leiaVastaskaar(kaar);
        return vastaskaar != null && vastaskaar.kaal == kaar.kaal;
    }

    private static Kaar leiaVastaskaar(Kaar kaar) {
        return kaar.lopp.kaared.stream()
                .filter(vastaskaar -> vastaskaar.lopp == kaar.algus)
                .findFirst()
                .orElse(null);
    }
}
