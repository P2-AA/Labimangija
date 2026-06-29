package ee.ut.labimangija.graphgrader.Kontrollerid;

import ee.ut.labimangija.graphgrader.Graaf.*;
import ee.ut.labimangija.graphgrader.Util.GraafiGenereerija;
import ee.ut.labimangija.graphgrader.Util.GraafiPaigutaja;
import ee.ut.labimangija.graphgrader.Util.GraafiValija;
import ee.ut.labimangija.graphgrader.Util.KaareKaaluKuvaja;
import ee.ut.labimangija.graphgrader.Util.KaarteKuhi;
import ee.ut.labimangija.graphgrader.Util.Logija;
import ee.ut.labimangija.ui.Popups;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class KruskalKontroller {

    public KaarteKuhi kuhi = new KaarteKuhi();
    public List<String> vead = new ArrayList<>(), sammud = new ArrayList<>();
    public Tipp[] esindajad;
    public int samm = 1;
    public Pane graafiElement;
    public Graaf g;
    public String failitee;
    public Button andmestruktuur, lukustaNupp, laeNupp;
    public HBox pseudoStruktuur;

    private void taastaYlesanne() {
        kuhi = new KaarteKuhi();
        vead.clear();
        sammud.clear();
        esindajad = null;
        samm = 1;
        graafiElement.getChildren().clear();
        pseudoStruktuur.getChildren().clear();
        andmestruktuur.setDisable(true);
        laeNupp.setVisible(true);
        lukustaNupp.setVisible(false);
    }

    public void laeGraaf(MouseEvent ignored) throws IOException {
        failitee = GraafiValija.valiFailVoiGenereeri(GraafiGenereerija.Tyyp.SIDUS_KAALUTUD);
        if (failitee == null)
            return;
        taastaYlesanne();
        g = new Graaf(failitee, false);
        naitaGraafi();
        laeNupp.setVisible(false);
        esindajad = new Tipp[g.tipud.size()];
        for (int i = 0; i < g.tipud.size(); i++)
            esindajad[i] = g.tipud.get(i);
        lukustaNupp.setVisible(true);
        andmestruktuur.setDisable(true);
    }

    public void naitaGraafi() {
        GraafiPaigutaja.paigutaRingina(g.tipud, graafiElement);
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(praeguneTipp.x, praeguneTipp.y, 30, praeguneTipp);
            praeguneTipp.tippGraafil = tippEkraanil;
            praeguneTipp.setToodeldud();
            graafiElement.getChildren().add(lisaTipuLiigutaja(tippEkraanil));
            for (Kaar k : praeguneTipp.kaared)
                if (k.algus.tähis.compareTo(k.lopp.tähis) < 0)
                    kuhi.lisa(k);
        }
        uuenda();
    }

    public Group lisaTipuLiigutaja(TippGraafil tipp) {
        Text tekst = new Text(tipp.tipp.tähis);
        GraafiPaigutaja.lisaLiigutamine(tipp, tekst, graafiElement, this::uuenda);

        Group g = new Group(tipp, tekst);
        tekst.toFront();
        return g;
    }

    public void kuvaStruktuurid() {
        int pikkus = 0;
        pseudoStruktuur.getChildren().clear();
        for (Kaar t : kuhi.kuhi) {
            if (pikkus > 60) {
                pseudoStruktuur.getChildren().add(new Text("\t ..."));
                break;
            }
            pseudoStruktuur.getChildren().add(new Text("\t %s%s:%d".formatted(t.algus.tähis, t.lopp.tähis, t.kaal)));
            pikkus += 6 + t.algus.tähis.length() + t.lopp.tähis.length() + String.valueOf(t.kaal).length();
        }
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);
        andmestruktuur.setDisable(false);
        for (Tipp t : g.tipud)
            t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);
        kuvaStruktuurid();
    }

    public void uuenda() {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        graafiElement.getChildren().removeIf(e -> e instanceof Text);
        List<Text> kaalud = new ArrayList<>();
        List<Arrow> kaared = new ArrayList<>();

        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                Arrow kaar = new Arrow(
                        k.algus.tippGraafil.getCenterX(), k.algus.tippGraafil.getCenterY(),
                        k.lopp.tippGraafil.getCenterX(), k.lopp.tippGraafil.getCenterY(),
                        true, true, k);
                k.arrow = kaar;
                kaared.add(kaar);

                if (g.kaalutud && KaareKaaluKuvaja.peaksKuvama(k))
                    kaalud.add(KaareKaaluKuvaja.looKaaluTekst(kaar, k, String.valueOf(kaar.kaar.kaal)));
            }
        }

        graafiElement.getChildren().addAll(kaared);
        graafiElement.getChildren().addAll(kaalud);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (kuhi.onTyhi()) {
            Logija.logi(vead, g, sammud, "Kruskal", true, false);
            Popups.showInfo("Läbimäng tehtud!\nKokku %d viga.\nLogi kirjutatud faili.".formatted(vead.size()));
            laeNupp.setVisible(true);
            andmestruktuur.setDisable(true);
            return;
        }
        Kaar min = kuhi.min();
        sammud.add(samm++ + "\t: Võtsin järjekorrast jägmise kaare " + min + ". KORRAS");
        kuvaStruktuurid();

        teeKaaredVarviliseks(min, Color.ORANGE);
        boolean kasKuulub = leiaKuuluvus(min);
        boolean vastus = kysiSisendit();
        while (vastus != kasKuulub) {
            sammud.add(samm + "\t: Küsin kaare toesesse kuulmist. VIGA");
            vead.add(samm++ + "\t: Kaar " + min + (kasKuulub ? " peaks " : " ei peaks ") + "toesesse kuuluma.");
            Popups.showError("Sain vale vastuse");
            vastus = kysiSisendit();
        }
        sammud.add(samm++ + "\t: Küsin kaare " + min + " toesesse kuulmist. KORRAS");
        if (kasKuulub)
            teeKaaredVarviliseks(min, Color.GREEN);
        else
            teeKaaredVarviliseks(min, Color.RED);
    }

    public void teeKaaredVarviliseks(Kaar k1, Color c) {
        k1.arrow.setFill(c);
        for (Kaar k : k1.lopp.kaared) {
            if (k.lopp == k1.algus) {
                k.arrow.setFill(c);
                return;
            }
        }
    }

    private boolean leiaKuuluvus(Kaar k) {
        Tipp a = leiaEsindaja(k.algus), b = leiaEsindaja(k.lopp);

        if (a != b)
            esindajad[a.tähis.charAt(0) - 'A'] = b;
        return a != b;
    }

    public Tipp leiaEsindaja(Tipp t) {
        int idx = t.tähis.charAt(0) - 'A';
        while (esindajad[idx] != t) {
            t = esindajad[idx];
            idx = t.tähis.charAt(0) - 'A';
        }
        return t;
    }

    public boolean kysiSisendit() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Sisend");
        dialog.setHeaderText("Kas valitud kaar lisada toesesse?");

        ButtonType jah = new ButtonType("Jah");
        ButtonType ei = new ButtonType("Ei");
        dialog.getButtonTypes().setAll(jah, ei);

        while (true) {
            Optional<ButtonType> valik = dialog.showAndWait();
            if (valik.isPresent()) {
                if (valik.get() == jah)
                    return true;
                if (valik.get() == ei)
                    return false;
            }
            Popups.showInfo("Vali palun 'Jah' või 'Ei'.");
        }
    }

}
