package ee.ut.labimangija.hashgrader;

import java.io.IOException;
import java.util.Locale;
import ee.ut.labimangija.common.Juhendid;
import ee.ut.labimangija.ui.Popups;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ee.ut.labimangija.hashgrader.samm.EemaldamiseSamm;
import ee.ut.labimangija.hashgrader.samm.LõpetamiseSamm;
import ee.ut.labimangija.hashgrader.samm.PaisktabeliLoomiseSamm;
import ee.ut.labimangija.hashgrader.samm.SisestamiseSamm;
import ee.ut.labimangija.hashgrader.ylesanne.EemaldamiseYlesanne;
import ee.ut.labimangija.hashgrader.ylesanne.KimbuYlesanne;
import ee.ut.labimangija.hashgrader.ylesanne.LisamiseYlesanne;
import ee.ut.labimangija.hashgrader.ylesanne.PositsiooniYlesanne;

public class HashGraderController {
    private enum Olek {
        YLESANDE_VALIK,
        ALGSEADISTUS,
        KASUD
    }

    @FXML
    private TabPane taskTabPane;

    @FXML
    private Tab lisamineTab;

    @FXML
    private Tab eemaldamineTab;

    @FXML
    private Tab kimbuTab;

    @FXML
    private Tab positsiooniTab;

    @FXML
    private TextArea terminalArea;

    @FXML
    private TextField setupField;

    @FXML
    private TextField indexField;

    @FXML
    private TextField rowField;

    @FXML
    private TextField slotField;

    @FXML
    private Label setupHelpLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button setupButton;

    @FXML
    private Button insertButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button finishButton;

    private Läbimäng labimang;
    private Olek olek;
    private String aktiivneTyyp;
    private String viimaneTeade;
    private String lukustatudAlgseadistus;
    private Integer positsiooniAlus;

    @FXML
    private void initialize() {
        taskTabPane.getSelectionModel().selectedItemProperty().addListener((obs, vana, uus) -> uuendaVaadet());
        taastaAlgseis();
    }

    @FXML
    private void handleStartTask() {
        try {
            alustaValitudYlesanne();
        } catch (IOException e) {
            viimaneTeade = "Ülesande laadimine ebaõnnestus: " + e.getMessage();
            taastaAlgseis();
        }
        uuendaVaadet();
    }

    @FXML
    private void handleResetTask() {
        taastaAlgseis();
    }

    @FXML
    private void handleShowGuide() {
        Popups.showInstructions(Juhendid.paisktabel(getSelectedGuideKey()));
    }

    @FXML
    private void handleSetupSubmit() {
        try {
            if (olek != Olek.ALGSEADISTUS) {
                throw new IllegalArgumentException("Algseadistust pole praegu vaja.");
            }
            String sisend = getTrimmedText(setupField);
            tootleAlgseadistus(sisend);
            if ("k".equals(aktiivneTyyp)) {
                lukustatudAlgseadistus = sisend;
                setupField.setText(lukustatudAlgseadistus);
            } else {
                setupField.clear();
            }
        } catch (IllegalArgumentException e) {
            viimaneTeade = e.getMessage();
        }
        uuendaVaadet();
    }

    @FXML
    private void handleInsert() {
        try {
            kontrolliKasudLubatud();
            sisesta(koostaKasuOsad("s"));
            puhastaSammuValjad();
        } catch (IllegalArgumentException e) {
            viimaneTeade = e.getMessage();
        }
        uuendaVaadet();
    }

    @FXML
    private void handleRemove() {
        try {
            kontrolliKasudLubatud();
            eemalda(koostaKasuOsad("e"));
            puhastaSammuValjad();
        } catch (IllegalArgumentException e) {
            viimaneTeade = e.getMessage();
        }
        uuendaVaadet();
    }

    @FXML
    private void handleUndo() {
        try {
            kontrolliKasudLubatud();
            votaTagasi(new String[] { "u" });
        } catch (IllegalArgumentException e) {
            viimaneTeade = e.getMessage();
        }
        uuendaVaadet();
    }

    @FXML
    private void handleFinish() {
        try {
            kontrolliKasudLubatud();
            lopeta();
        } catch (IllegalArgumentException e) {
            viimaneTeade = e.getMessage();
        }
        uuendaVaadet();
    }

    private void alustaValitudYlesanne() throws IOException {
        String valitudTyyp = getSelectedTaskType();
        String sisendiFail = HashSisendiValija.valiSisend(valitudTyyp);
        if (sisendiFail == null) {
            viimaneTeade = "Ülesande alustamine katkestati.";
            return;
        }

        aktiivneTyyp = valitudTyyp;
        labimang = new Läbimäng();
        labimang.setHindaja(new Hindaja());

        switch (aktiivneTyyp) {
            case "l" -> labimang.setYlesanne(new LisamiseYlesanne(sisendiFail));
            case "e" -> labimang.setYlesanne(new EemaldamiseYlesanne(sisendiFail));
            case "k" -> labimang.setYlesanne(new KimbuYlesanne(sisendiFail));
            case "p" -> labimang.setYlesanne(new PositsiooniYlesanne(sisendiFail));
            default -> throw new IllegalStateException("Toetamata ülesande tüüp: " + aktiivneTyyp);
        }

        olek = ("k".equals(aktiivneTyyp) || "p".equals(aktiivneTyyp)) ? Olek.ALGSEADISTUS : Olek.KASUD;
        viimaneTeade = null;
        lukustatudAlgseadistus = null;
        positsiooniAlus = null;
        setupField.clear();
        puhastaSammuValjad();
    }

    private void kontrolliKasudLubatud() {
        if (olek == Olek.YLESANDE_VALIK) {
            throw new IllegalArgumentException("Ava enne valitud ülesanne.");
        }
        if (olek == Olek.ALGSEADISTUS) {
            throw new IllegalArgumentException("Salvesta enne algseadistus.");
        }
    }

    private String[] koostaKasuOsad(String kask) {
        String valitudTyyp = getSelectedTaskType();
        String i = ("l".equals(valitudTyyp) || "e".equals(valitudTyyp)) ? "0" : getTrimmedText(indexField);
        String r = getTrimmedText(rowField);
        String k = getTrimmedText(slotField);

        if (i.isEmpty() || r.isEmpty()) {
            throw new IllegalArgumentException("Sisesta väljad i ja r.");
        }

        return k.isEmpty() ? new String[] { kask, i, r } : new String[] { kask, i, r, k };
    }

    private String getSelectedTaskType() {
        Tab selectedTab = taskTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == lisamineTab) {
            return "l";
        }
        if (selectedTab == eemaldamineTab) {
            return "e";
        }
        if (selectedTab == kimbuTab) {
            return "k";
        }
        if (selectedTab == positsiooniTab) {
            return "p";
        }
        throw new IllegalStateException("Ülesande vaade puudub.");
    }

    private String getSelectedGuideKey() {
        Tab selectedTab = taskTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == lisamineTab) {
            return "lisamine";
        }
        if (selectedTab == eemaldamineTab) {
            return "eemaldamine";
        }
        if (selectedTab == kimbuTab) {
            return "kimp";
        }
        return "positsioon";
    }

    private void tootleAlgseadistus(String sisend) {
        if (sisend.isEmpty()) {
            throw new IllegalArgumentException("Sisesta algseadistus enne jätkamist.");
        }

        String[] osad = sisend.split("\\s+");
        boolean onnestus;

        if ("p".equals(aktiivneTyyp)) {
            if (osad.length != 1) {
                throw new IllegalArgumentException("Positsioonimeetodi jaoks sisesta üks arv: paisktabeli pikkus.");
            }
            int pikkus = parseInt(osad[0], "Paisktabeli pikkus peab olema täisarv.");
            if (pikkus <= 0) {
                throw new IllegalArgumentException("Paisktabeli pikkus peab olema suurem kui 0.");
            }
            onnestus = labimang.astu(new PaisktabeliLoomiseSamm(pikkus));
            positsiooniAlus = pikkus;
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
            onnestus = labimang.astu(new PaisktabeliLoomiseSamm(a, b, m));
        } else {
            throw new IllegalStateException("Algseadistust oodati vales olekus.");
        }

        if (!onnestus) {
            throw new IllegalArgumentException("Paisktabeli loomine ebaõnnestus antud väärtustega.");
        }

        olek = Olek.KASUD;
        viimaneTeade = "Algseadistus salvestati.";
    }

    private void lopeta() {
        if (!labimang.astu(new LõpetamiseSamm())) {
            throw new IllegalArgumentException("Algoritmi lõpetamine ebaõnnestus.");
        }

        viimaneTeade = "Hinne: " + String.format(Locale.US, "%.2f", labimang.getPunktid()) + "%";
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

        boolean onnestus = labimang.astu(new SisestamiseSamm<>(i, r, k));
        viimaneTeade = onnestus ? "Sisestamise samm salvestati." : "Sisestamine ebaõnnestus.";
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

        boolean onnestus = labimang.astu(new EemaldamiseSamm<>(i, r, k));
        viimaneTeade = onnestus ? "Eemaldamise samm salvestati." : "Eemaldamine ebaõnnestus.";
    }

    private void votaTagasi(String[] osad) {
        if (osad.length != 1) {
            throw new IllegalArgumentException("Tagasivõtmise käsk on lihtsalt: u");
        }

        boolean tyhiAjalugu = labimang.tagasi();
        if (tyhiAjalugu) {
            viimaneTeade = "Kõik sammud said tagasi võetud.";
            taastaAlgseis();
        } else {
            viimaneTeade = "Viimane samm võeti tagasi.";
        }
    }

    private int parseInt(String vaartus, String veateade) {
        try {
            return Integer.parseInt(vaartus);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(veateade);
        }
    }

    private float parseFloat(String vaartus, String veateade) {
        try {
            return Float.parseFloat(vaartus);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(veateade);
        }
    }

    private String getTrimmedText(TextField textField) {
        return textField.getText() == null ? "" : textField.getText().trim();
    }

    private void puhastaSammuValjad() {
        indexField.clear();
        rowField.clear();
        slotField.clear();
    }

    private void taastaAlgseis() {
        labimang = null;
        aktiivneTyyp = null;
        lukustatudAlgseadistus = null;
        positsiooniAlus = null;
        olek = Olek.YLESANDE_VALIK;
        setupField.clear();
        puhastaSammuValjad();
        uuendaVaadet();
    }

    private void uuendaVaadet() {
        terminalArea.setText(koostaEkraan());
        statusLabel.setText(viimaneTeade == null ? "Valmis alustama." : viimaneTeade);
        String valitudTyyp = getSelectedTaskType();

        boolean setupVisible = olek == Olek.ALGSEADISTUS;
        setupField.setDisable(!setupVisible);
        setupButton.setDisable(!setupVisible);
        if (!setupVisible && "k".equals(aktiivneTyyp) && lukustatudAlgseadistus != null) {
            setupField.setText(lukustatudAlgseadistus);
        }

        boolean commandsEnabled = olek == Olek.KASUD;
        boolean lisamineSakk = "l".equals(valitudTyyp);
        boolean eemaldamineSakk = "e".equals(valitudTyyp);
        insertButton.setDisable(!commandsEnabled || eemaldamineSakk);
        removeButton.setDisable(!commandsEnabled || lisamineSakk);
        undoButton.setDisable(!commandsEnabled);
        finishButton.setDisable(!commandsEnabled);
        indexField.setDisable(!commandsEnabled || lisamineSakk || eemaldamineSakk);
        rowField.setDisable(!commandsEnabled);
        slotField.setDisable(!commandsEnabled);

        if (lisamineSakk || eemaldamineSakk) {
            indexField.setText("0");
        } else if (!commandsEnabled) {
            indexField.clear();
        }

        setupHelpLabel.setText(koostaAlgseadistuseAbi());

        if (setupVisible) {
            setupField.requestFocus();
        } else if (commandsEnabled) {
            indexField.requestFocus();
        }
    }

    private String koostaAlgseadistuseAbi() {
        String valitudTyyp = getSelectedTaskType();
        return switch (valitudTyyp) {
            case "k" -> "Kimbumeetod vajab kolmikut a b m.\na - minimaalne element,\nb - maksimaalne element,\nm - kimpude arv.";
            case "p" -> positsiooniAlus == null
                    ? "Positsioonimeetod vajab algseadistusena paisktabeli pikkust."
                    : "Meetodi alus on: " + positsiooniAlus;
            default -> "Lisamine ja eemaldamine ei vaja eraldi algseadistust.";
        };
    }

    private String koostaEkraan() {
        StringBuilder sb = new StringBuilder();

        if (labimang != null) {
            sb.append(labimang.ylesandeKirjeldus()).append("\n");
            sb.append("-----------------------------------------------------------\n\n");
        } else {
            // sb.append("Valitud sakk:
            // ").append(tabPealkiri(getSelectedTaskType())).append("\n\n");
            sb.append("Vali ülevalt algoritm ja vajuta \"Alusta\".");
        }

        if (olek == Olek.ALGSEADISTUS) {
            if ("p".equals(aktiivneTyyp)) {
                sb.append("Sisesta paisktabeli pikkus.");
            } else {
                sb.append(
                        "Sisesta a = minimaalne element, b = maksimaalne element ja\nm = kimpude arv (eraldatud tühikutega).\n");
            }
        }

        if (olek == Olek.KASUD && labimang != null) {
            sb.append("töödeldav alamjärjend: ").append(labimang.getAbijärjend()).append("\n");
            sb.append("paisktabel:\n").append(labimang.getPaisktabel()).append("\n\n");

            /*
             * sb.append("Toimingud:\n");
             * sb.append("Sisesta: vii element massiivist paisktabelisse.\n");
             * sb.append("Eemalda: too element paisktabelist tagasi massiivi.\n");
             * sb.append("Vota tagasi: tuhista viimane samm.\n");
             * sb.append("Lopeta: hinda lahendus ja alusta uuesti.\n");
             */
        }

        return sb.toString().trim();
    }

    private String tabPealkiri(String tyyp) {
        return switch (tyyp) {
            case "l" -> "Lisamine";
            case "e" -> "Eemaldamine";
            case "k" -> "Kimbumeetod";
            case "p" -> "Positsioonimeetod";
            default -> "HashGrader";
        };
    }
}
