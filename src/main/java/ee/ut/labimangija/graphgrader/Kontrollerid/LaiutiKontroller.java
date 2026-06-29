package ee.ut.labimangija.graphgrader.Kontrollerid;

import ee.ut.labimangija.graphgrader.Graaf.*;
import ee.ut.labimangija.graphgrader.Util.GraafiGenereerija;
import ee.ut.labimangija.graphgrader.Util.GraafiPaigutaja;
import ee.ut.labimangija.graphgrader.Util.GraafiValija;
import ee.ut.labimangija.graphgrader.Util.Logija;
import ee.ut.labimangija.ui.Popups;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class LaiutiKontroller {

    public List<Tipp> jarjekord = new ArrayList<>(), toodeldud = new ArrayList<>();
    public List<String> sammud = new ArrayList<>(), vead = new ArrayList<>();

    public Pane graafiElement;
    public Graaf g;
    public String failitee;
    public Button andmestruktuur, laeNupp, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;
    public int samm = 1;

    private void taastaYlesanne() {
        jarjekord.clear();
        toodeldud.clear();
        sammud.clear();
        vead.clear();
        samm = 1;
        graafiElement.getChildren().clear();
        pseudoStruktuur.getChildren().clear();
        pseudoToodeldud.getChildren().clear();
        andmestruktuur.setDisable(true);
        laeNupp.setVisible(true);
        lukustaNupp.setVisible(false);
    }

    public void laeGraaf(MouseEvent ignored) throws IOException {
        failitee = GraafiValija.valiFailVoiGenereeri(GraafiGenereerija.Tyyp.LABIMINE);
        if (failitee == null) return;
        taastaYlesanne();
        g = new Graaf(failitee, true);
        naitaGraafi();
        laeNupp.setVisible(false);
        andmestruktuur.setDisable(true);
        lukustaNupp.setVisible(true);
    }

    public void naitaGraafi() {
        GraafiPaigutaja.paigutaRingina(g.tipud, graafiElement);
        for (int i = 0; i < g.tipud.size(); i++) {
            Tipp praeguneTipp = g.tipud.get(i);
            TippGraafil tippEkraanil = new TippGraafil(praeguneTipp.x, praeguneTipp.y, 30, praeguneTipp);
            tippEkraanil.setFill(Color.WHITE);
            praeguneTipp.tippGraafil = tippEkraanil;
            if (i == 0) praeguneTipp.setPraegune();

            graafiElement.getChildren().add(lisaTipuLiigutaja(tippEkraanil));
        }
        uuenda();
    }

    private Tipp leiaPraegune() {
        for (Tipp tipp : g.tipud)
            if (tipp.seis == TipuSeis.PRAEGUNE)
                return tipp;
        return null;
    }

    public Group lisaTipuLiigutaja(TippGraafil tipp) {
        Text tekst = new Text(tipp.tipp.tähis);
        GraafiPaigutaja.lisaLiigutamine(tipp, tekst, graafiElement, this::uuenda);

        return new Group(tipp, tekst);
    }

    public void kuvaStruktuurid() {
        pseudoStruktuur.getChildren().clear();
        for (Tipp t : jarjekord) pseudoStruktuur.getChildren().add(new Text("\t" + t.tähis));

        pseudoToodeldud.getChildren().clear();
        for (Tipp t : toodeldud) pseudoToodeldud.getChildren().add(new Text("\t" + t.tähis));
    }

    public void lukustaGraaf(MouseEvent ignored) {
        lukustaNupp.setVisible(false);

        for (Tipp t : g.tipud) {
            t.tippGraafil.addEventFilter(MouseEvent.MOUSE_DRAGGED, MouseEvent::consume);
            lisaKontrollija(t.tippGraafil);
        }
    }

    public void lisaKontrollija(TippGraafil tipp) {
        tipp.setOnMouseClicked(e -> {
            if (tipp.tipp.seis == TipuSeis.PRAEGUNE) {
                String kontrolliTulemus = kontrolli(tipp);
                if (kontrolliTulemus.equals("")) {
                    sammud.add(samm++ + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". KORRAS");
                    tipp.tipp.setToodeldud();
                    if (toodeldud.contains(tipp.tipp)) return;
                    toodeldud.add(tipp.tipp);
                    kuvaStruktuurid();
                    andmestruktuur.setDisable(false);
                    return;
                }
                sammud.add(samm + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". VIGA");
                vead.add(samm++ + "\t: " + kontrolliTulemus);
                Popups.showError(kontrolliTulemus);
            } else {
                Tipp praegune = leiaPraegune();
                Tipp jarglane = null;
                if (praegune == null) return;
                for (Tipp t : praegune.alluvad) if (t == tipp.tipp) {jarglane = t;break;}
                if (jarglane == null) {
                    sammud.add(samm + "\t: Lisan tipu " + tipp.tipp.tähis + " järjekorda. VIGA");
                    vead.add(samm++ + "\t: Lõpptipp " + tipp.tipp.tähis + " ei ole aktiivse tipu järglane.");
                    Popups.showError("Lõpptipp " + tipp.tipp.tähis + " ei ole aktiivse tipu järglane.");
                    return;
                }
                if (jarglane.seis == TipuSeis.AVASTAMATA) {
                    jarjekord.add(jarglane);
                    sammud.add(samm++ + "\t: Lisan tipu " + jarglane.tähis + " järjekorda. KORRAS");
                    jarglane.setAndmestruktuuris();
                    kuvaStruktuurid();
                } else if (jarglane.seis == TipuSeis.ANDMESTRUKTUURIS || jarglane.seis == TipuSeis.TÖÖDELDUD) {
                    sammud.add(samm + "\t: Lisan tipu " + jarglane.tähis + " järjekorda. VIGA");
                    vead.add(samm++ + "\t: Lõpptipp " + jarglane.tähis + " on juba töödeldud või andmestruktuuris.");
                    Popups.showError("Lõpptipp " + jarglane.tähis + " on juba töödeldud või andmestruktuuris.");
                }
            }
        });
    }

    public void uuenda() {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        List<Arrow> kaared = new ArrayList<>();

        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                Arrow kaar = new Arrow(
                        k.algus.tippGraafil.getCenterX(), k.algus.tippGraafil.getCenterY(),
                        k.lopp.tippGraafil.getCenterX(), k.lopp.tippGraafil.getCenterY(),
                        true, false, k
                );
                kaared.add(kaar);
            }
        }

        graafiElement.getChildren().addAll(kaared);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (jarjekord.isEmpty()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud, "Laiuti", false, false);
                Popups.showInfo("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()));
                laeNupp.setVisible(true);
            }
            andmestruktuur.setDisable(true);
            return;
        }
        Tipp t = jarjekord.remove(0);
        sammud.add(samm++ + "\t: Võtsin järjekorrast järgmise tipu " + t.tähis + ". KORRAS");
        if (t.seis != TipuSeis.TÖÖDELDUD) t.setPraegune();
        if (t.seis != TipuSeis.TÖÖDELDUD) andmestruktuur.setDisable(true);
        kuvaStruktuurid();
    }

    public String kontrolli(TippGraafil t) {
        Tipp tipp = t.tipp;
        if (tipp.seis != TipuSeis.PRAEGUNE) return "Tipp %s ei ole praegu töödeldav".formatted(tipp.tähis);
        for (Tipp alluv : tipp.alluvad)
            if (alluv.seis != TipuSeis.ANDMESTRUKTUURIS && alluv.seis != TipuSeis.TÖÖDELDUD)
                return "Kõik järglased ei ole töödeldud.";

        return "";
    }
}

