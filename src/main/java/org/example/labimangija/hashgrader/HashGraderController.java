package org.example.labimangija.hashgrader;

import java.io.IOException;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.labimangija.hashgrader.samm.EemaldamiseSamm;
import org.example.labimangija.hashgrader.samm.LõpetamiseSamm;
import org.example.labimangija.hashgrader.samm.PaisktabeliLoomiseSamm;
import org.example.labimangija.hashgrader.samm.SisestamiseSamm;
import org.example.labimangija.hashgrader.ylesanne.EemaldamiseYlesanne;
import org.example.labimangija.hashgrader.ylesanne.KimbuYlesanne;
import org.example.labimangija.hashgrader.ylesanne.LisamiseYlesanne;
import org.example.labimangija.hashgrader.ylesanne.PositsiooniYlesanne;

public class HashGraderController {
    private static final String LISAMINE_EEMALDAMINE = "sisendid/lisamineEemaldamine/sisend.txt";
    private static final String KIMBU_MEETOD = "sisendid/kimbumeetod/sisend.txt";
    private static final String POSITSIOONI_MEETOD = "sisendid/positsioonimeetod/sisend.txt";

    private enum Olek {
        YLESANDE_VALIK,
        ALGSEADISTUS,
        KASUD
    }

    @FXML
    private TextArea terminalArea;

    @FXML
    private TextField inputField;

    @FXML
    private Label statusLabel;

    private Läbimäng läbimäng;
    private Olek olek;
    private String aktiivneTyyp;
    private String viimaneTeade;

    @FXML
    private void initialize() {
        taastaAlgseis();
    }

    @FXML
    private void handleSubmit() {
        String sisend = inputField.getText() == null ? "" : inputField.getText().trim();
        inputField.clear();

        try {
            switch (olek) {
                case YLESANDE_VALIK -> tootleYlesandeValik(sisend);
                case ALGSEADISTUS -> tootleAlgseadistus(sisend);
                case KASUD -> tootleKask(sisend);
            }
        } catch (IOException e) {
            viimaneTeade = "Ülesande laadimine ebaõnnestus: " + e.getMessage();
            taastaAlgseis();
        } catch (IllegalArgumentException e) {
            viimaneTeade = e.getMessage();
        }

        uuendaVaadet();
    }

    private void tootleYlesandeValik(String sisend) throws IOException {
        if (sisend.isEmpty()) {
            throw new IllegalArgumentException("Sisesta ülesande tüüp: l, e, k või p.");
        }

        String valik = sisend.toLowerCase(Locale.ROOT);
        if (!valik.matches("[lekpx]")) {
            throw new IllegalArgumentException("Tundmatu ülesande tüüp. Kasuta l, e, k, p või x.");
        }
        if ("x".equals(valik)) {
            taastaAlgseis();
            viimaneTeade = "Sulgemiskäsku ei kasutata rakenduse sees. Vali l, e, k või p.";
            return;
        }

        aktiivneTyyp = valik;
        läbimäng = new Läbimäng();
        läbimäng.setHindaja(new Hindaja());

        switch (aktiivneTyyp) {
            case "l" -> läbimäng.setYlesanne(new LisamiseYlesanne(LISAMINE_EEMALDAMINE));
            case "e" -> läbimäng.setYlesanne(new EemaldamiseYlesanne(LISAMINE_EEMALDAMINE));
            case "k" -> läbimäng.setYlesanne(new KimbuYlesanne(KIMBU_MEETOD));
            case "p" -> läbimäng.setYlesanne(new PositsiooniYlesanne(POSITSIOONI_MEETOD));
            default -> throw new IllegalStateException("Toetamata ülesande tüüp: " + aktiivneTyyp);
        }

        if ("k".equals(aktiivneTyyp) || "p".equals(aktiivneTyyp)) {
            olek = Olek.ALGSEADISTUS;
            viimaneTeade = null;
        } else {
            olek = Olek.KASUD;
            viimaneTeade = null;
        }
    }

    private void tootleAlgseadistus(String sisend) {
        if (sisend.isEmpty()) {
            throw new IllegalArgumentException("Sisesta algseadistus enne jätkamist.");
        }

        String[] osad = sisend.split("\\s+");
        boolean õnnestus;

        if ("p".equals(aktiivneTyyp)) {
            if (osad.length != 1) {
                throw new IllegalArgumentException("Positsioonimeetodi jaoks sisesta üks arv: paisktabeli pikkus.");
            }
            int pikkus = parseInt(osad[0], "Paisktabeli pikkus peab olema täisarv.");
            if (pikkus <= 0) {
                throw new IllegalArgumentException("Paisktabeli pikkus peab olema suurem kui 0.");
            }
            õnnestus = läbimäng.astu(new PaisktabeliLoomiseSamm(pikkus));
        } else if ("k".equals(aktiivneTyyp)) {
            if (osad.length != 3) {
                throw new IllegalArgumentException("Kimbumeetodi jaoks sisesta kolm väärtust kujul: a b m.");
            }
            float a = parseFloat(osad[0], "a peab olema arv.");
            float b = parseFloat(osad[1], "b peab olema arv.");
            int m = parseInt(osad[2], "m peab olema täisarv.");
            if (m <= 0) {
                throw new IllegalArgumentException("m peab olema suurem kui 0.");
            }
            if (a == b) {
                throw new IllegalArgumentException("a ja b ei tohi olla võrdsed.");
            }
            õnnestus = läbimäng.astu(new PaisktabeliLoomiseSamm(a, b, m));
        } else {
            throw new IllegalStateException("Algseadistust oodati vales olekus.");
        }

        if (!õnnestus) {
            throw new IllegalArgumentException("Paisktabeli loomine ebaõnnestus antud väärtustega.");
        }

        olek = Olek.KASUD;
        viimaneTeade = "Algseadistus salvestati.";
    }

    private void tootleKask(String sisend) {
        if (sisend.isEmpty()) {
            throw new IllegalArgumentException("Sisesta käsk.");
        }

        String[] osad = sisend.split("\\s+");
        switch (osad[0].toLowerCase(Locale.ROOT)) {
            case "l" -> lõpeta();
            case "s" -> sisesta(osad);
            case "e" -> eemalda(osad);
            case "u" -> võtaTagasi(osad);
            default -> throw new IllegalArgumentException("Tundmatu käsk. Kasuta l, s, e või u.");
        }
    }

    private void lõpeta() {
        if (!läbimäng.astu(new LõpetamiseSamm())) {
            throw new IllegalArgumentException("Algoritmi lõpetamine ebaõnnestus.");
        }

        viimaneTeade = "Hinne: " + String.format(Locale.US, "%.2f", läbimäng.getPunktid()) + "%";
        taastaAlgseis();
    }

    private void sisesta(String[] osad) {
        if (osad.length != 3 && osad.length != 4) {
            throw new IllegalArgumentException("Sisestamise käsk on kujul: s i r (k).");
        }

        int i = parseInt(osad[1], "Indeks i peab olema täisarv.");
        int r = parseInt(osad[2], "Rida r peab olema täisarv.");
        int k = osad.length == 4 ? parseInt(osad[3], "Koht k peab olema täisarv.") : 0;
        if (i < 0 || r < 0 || k < 0) {
            throw new IllegalArgumentException("i, r ja k peavad olema mittenegatiivsed.");
        }

        boolean õnnestus = läbimäng.astu(new SisestamiseSamm<>(i, r, k));
        viimaneTeade = õnnestus ? "Sisestamise samm salvestati." : "Sisestamine ebaõnnestus.";
    }

    private void eemalda(String[] osad) {
        if (osad.length != 3 && osad.length != 4) {
            throw new IllegalArgumentException("Eemaldamise käsk on kujul: e i r (k).");
        }

        int i = parseInt(osad[1], "Indeks i peab olema täisarv.");
        int r = parseInt(osad[2], "Rida r peab olema täisarv.");
        int k = osad.length == 4 ? parseInt(osad[3], "Koht k peab olema täisarv.") : 0;
        if (i < 0 || r < 0 || k < 0) {
            throw new IllegalArgumentException("i, r ja k peavad olema mittenegatiivsed.");
        }

        boolean õnnestus = läbimäng.astu(new EemaldamiseSamm<>(i, r, k));
        viimaneTeade = õnnestus ? "Eemaldamise samm salvestati." : "Eemaldamine ebaõnnestus.";
    }

    private void võtaTagasi(String[] osad) {
        if (osad.length != 1) {
            throw new IllegalArgumentException("Tagasivõtmise käsk on lihtsalt: u");
        }

        boolean tühiAjalugu = läbimäng.tagasi();
        if (tühiAjalugu) {
            viimaneTeade = "Kõik sammud said tagasi võetud.";
            taastaAlgseis();
        } else {
            viimaneTeade = "Viimane samm võeti tagasi.";
        }
    }

    private int parseInt(String väärtus, String veateade) {
        try {
            return Integer.parseInt(väärtus);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(veateade);
        }
    }

    private float parseFloat(String väärtus, String veateade) {
        try {
            return Float.parseFloat(väärtus);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(veateade);
        }
    }

    private void taastaAlgseis() {
        läbimäng = null;
        aktiivneTyyp = null;
        olek = Olek.YLESANDE_VALIK;
        uuendaVaadet();
    }

    private void uuendaVaadet() {
        terminalArea.setText(koostaEkraan());
        statusLabel.setText(viimaneTeade == null ? " " : viimaneTeade);
        inputField.requestFocus();
    }

    private String koostaEkraan() {
        StringBuilder sb = new StringBuilder();

        if (läbimäng != null) {
            sb.append(läbimäng.ylesandeKirjeldus()).append("\n");
            sb.append("-----------------------------------------------------------\n\n");
        } else {
            sb.append("ALGUS\n\n");
        }

        switch (olek) {
            case YLESANDE_VALIK -> {
                sb.append("""
                        l - lisamine
                        e - eemaldamine
                        k - kimbumeetod
                        p - positsioonimeetod
                        x - välju
                        """);
                sb.append("\nVali ülesande tüüp: ");
            }
            case ALGSEADISTUS -> {
                if ("p".equals(aktiivneTyyp)) {
                    sb.append("Sisesta paisktabeli pikkus: ");
                } else {
                    sb.append("Sisesta a b m (eraldatud tühikutega): ");
                }
            }
            case KASUD -> {
                sb.append("töödeldav alamjärjend: ").append(läbimäng.getAbijärjend()).append("\n");
                sb.append("paisktabel:\n").append(läbimäng.getPaisktabel()).append("\n");
                sb.append("""
                        Vali käsk:
                        l - algoritm lõpetab
                        s <i> <r> (<k>) - sisesta element massiivist indeksilt i paisktabelisse reale r (kohale k)
                        e <i> <r> (<k>) - eemalda paisktabelist realt r (kohalt k) element ja pane see massiivi indeksile i
                        u - võta samm tagasi
                        """);
            }
        }

        return sb.toString().trim();
    }
}
