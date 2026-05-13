package ee.ut.labimangija.graphgrader.Kontrollerid;

import ee.ut.labimangija.graphgrader.Graaf.*;
import ee.ut.labimangija.graphgrader.Util.GraafiGenereerija;
import ee.ut.labimangija.graphgrader.Util.GraafiPaigutaja;
import ee.ut.labimangija.graphgrader.Util.GraafiValija;
import ee.ut.labimangija.graphgrader.Util.KaarteKuhi;
import ee.ut.labimangija.graphgrader.Util.Logija;
import ee.ut.labimangija.graphgrader.Util.Teavitaja;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class PrimKontroller {

    public KaarteKuhi kuhi = new KaarteKuhi();
    public List<Tipp> toodeldud = new ArrayList<>();
    public List<String> sammud = new ArrayList<>(), vead = new ArrayList<>();
    public List<Kaar> ootel = new ArrayList<>(), kasutatud = new ArrayList<>();

    public Pane graafiElement;
    public Graaf g;
    public String failitee;
    public Button andmestruktuur, laeNupp, lukustaNupp;
    public HBox pseudoStruktuur, pseudoToodeldud;
    public int samm = 1;

    private void taastaYlesanne() {
        kuhi = new KaarteKuhi();
        toodeldud.clear();
        sammud.clear();
        vead.clear();
        ootel.clear();
        kasutatud.clear();
        samm = 1;
        graafiElement.getChildren().clear();
        pseudoStruktuur.getChildren().clear();
        pseudoToodeldud.getChildren().clear();
        andmestruktuur.setDisable(true);
        laeNupp.setVisible(true);
        lukustaNupp.setVisible(false);
    }

    public void laeGraaf(MouseEvent ignored) throws IOException {
        failitee = GraafiValija.valiFailVoiGenereeri("sisendid/graafid/suunatud_kaalutud", GraafiGenereerija.Tyyp.SIDUS_KAALUTUD);
        if (failitee == null) return;
        taastaYlesanne();
        g = new Graaf(failitee, false);
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

            graafiElement.getChildren().add(lisaTipuKasitleja(tippEkraanil));
        }
        uuenda();
    }

    public Group lisaTipuKasitleja(TippGraafil tipp) {
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
            } else {
                pseudoStruktuur.getChildren().add(new Text("\t %s%s:%d".formatted(t.algus.tähis, t.lopp.tähis, t.kaal)));
                pikkus += 6 + t.algus.tähis.length() + t.lopp.tähis.length() + String.valueOf(t.kaal).length();
            }
        }

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
        tipp.setOnMouseClicked(e -> { // Klikk ehk kontrollimine
            if (tipp.tipp.seis == TipuSeis.PRAEGUNE) {
                String kontrolliTulemus = kontrolli(tipp);
                if (kontrolliTulemus.equals("")) {
                    sammud.add(samm++ + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". KORRAS");
                    tipp.tipp.setToodeldud();
                    toodeldud.add(tipp.tipp);
                    kuvaStruktuurid();
                    andmestruktuur.setDisable(false);
                    return;
                }
                sammud.add(samm + "\t: Kontrollin tippu " + tipp.tipp.tähis + ". VIGA");
                vead.add(samm++ + "\t: " + kontrolliTulemus);
                Teavitaja.teavita(kontrolliTulemus, "Viga");
            } else {
                Tipp praegune = leiaPraegune();
                if (praegune == null) return;
                Kaar esimene = null, teine = null;
                for (Kaar kaar : praegune.kaared) if (kaar.lopp == tipp.tipp) esimene = kaar;
                for (Kaar kaar : tipp.tipp.kaared) if (kaar.lopp == praegune) teine = kaar;
                if (esimene == null || teine == null) {
                    sammud.add(samm + "\t: Lisan serva " + praegune.tähis + "-" + tipp.tipp.tähis + " järjekorda. VIGA");
                    vead.add(samm++ + "\t: Tipp " + tipp.tipp.tähis + " ei ole praeguse tipu " + praegune.tähis + " järglane.");
                    Teavitaja.teavita("Valitud tipp ei ole aktiivse tipu järglane.", "Viga");
                    return;
                }
                if (tipp.tipp.seis == TipuSeis.ANDMESTRUKTUURIS || tipp.tipp.seis == TipuSeis.AVASTAMATA) {
                    if (kuhi.sisaldab(esimene) || kuhi.sisaldab(teine)) {
                        sammud.add(samm + "\t: Lisan serva " + esimene + " järjekorda. VIGA");
                        vead.add(samm++ + "\t: Serv on järjekorras juba olemas.");
                        Teavitaja.teavita("Serva topelt lisamine", "Viga");
                        return;
                    }
                    sammud.add(samm++ + "\t: Lisan serva " + esimene + " järjekorda. KORRAS");
                    kuhi.lisa(esimene);
                    tipp.tipp.setAndmestruktuuris();
                    esimene.arrow.setFill(Color.ORANGE);
                    teine.arrow.setFill(Color.ORANGE);
                    ootel.add(esimene);
                    ootel.add(teine);
                    kuvaStruktuurid();
                } else if (tipp.tipp.seis == TipuSeis.TÖÖDELDUD) {
                    sammud.add(samm + "\t: Lisan serva " + esimene + " järjekorda. VIGA");
                    vead.add(samm++ + "\t: Serva lõpptipp " + tipp.tipp.tähis + " on juba töödeldud.");
                    Teavitaja.teavita("Serva lõpptipp on juba töödeldud.", "Viga");
                }
            }
        });
    }

    private Tipp leiaPraegune() {
        for (Tipp tipp : g.tipud)
            if (tipp.seis == TipuSeis.PRAEGUNE)
                return tipp;
        return null;
    }

    public void uuenda() {
        graafiElement.getChildren().removeIf(e -> e instanceof Arrow);
        graafiElement.getChildren().removeIf(e -> e instanceof Text);
        List<Text> kaalud = new ArrayList<>();
        List<Arrow> kaared = new ArrayList<>();
        Map<Integer, Integer> kaaludeKordused = loendaKaaludeKordused();

        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                Arrow kaar = new Arrow(
                        k.algus.tippGraafil.getCenterX(), k.algus.tippGraafil.getCenterY(),
                        k.lopp.tippGraafil.getCenterX(), k.lopp.tippGraafil.getCenterY(),
                        true, true, k
                );
                k.arrow = kaar;
                kaared.add(kaar);

                if (g.kaalutud)
                    kaalud.add(new Text(kaar.midX, kaar.midY, kuvatavKaal(k, kaaludeKordused)));
            }
        }

        graafiElement.getChildren().addAll(kaared);
        graafiElement.getChildren().addAll(kaalud);
    }

    public void votaAndmestruktuurist(MouseEvent ignored) {
        if (kuhi.onTyhi()) {
            if (toodeldud.size() == g.tipud.size()) {
                Logija.logi(vead, g, sammud, "Prim", true, false);
                Teavitaja.teavita("Läbimäng tehtud!\nKokku %d viga.\nLogi faili kirjutatud.".formatted(vead.size()), "Info");
                laeNupp.setVisible(true);
            }
            andmestruktuur.setDisable(true);
            return;
        }
        Kaar min = kuhi.min();
        sammud.add(samm++ + "\t: Võtsin järjekorrast järgmise kaare " + min + ". KORRAS");
        kuvaStruktuurid();
        if (min.lopp.seis == TipuSeis.TÖÖDELDUD) return;
        min.arrow.setFill(Color.GREEN);
        min.lopp.setPraegune();
        kasutatud.add(min);
        for (Kaar k : min.lopp.kaared) {
            if (k.lopp == min.algus) {
                k.arrow.setFill(Color.GREEN);
                kasutatud.add(k);
                break;
            }
        }
        andmestruktuur.setDisable(true);
    }

    public String kontrolli(TippGraafil t) {
        Tipp tipp = t.tipp;
        if (tipp.seis == TipuSeis.TÖÖDELDUD) return "Tipp {%s} on töödeldud".formatted(tipp.tähis);
        if (tipp.seis != TipuSeis.PRAEGUNE) return "Tipp {%s} ei ole praegu töödeldav".formatted(tipp.tähis);
        for (Kaar kaar : tipp.kaared)
            if (kaar.lopp.seis != TipuSeis.TÖÖDELDUD && kaar.lopp.seis != TipuSeis.ANDMESTRUKTUURIS)
                return "Järglane {%s} ei ole töödeldud ega andmestruktuuris".formatted(kaar.lopp.tähis);

        return "";
    }

    private Map<Integer, Integer> loendaKaaludeKordused() {
        Map<Integer, Integer> kordused = new HashMap<>();
        for (Tipp t : g.tipud) {
            for (Kaar k : t.kaared) {
                if (k.algus.tähis.compareTo(k.lopp.tähis) < 0) {
                    kordused.merge(k.kaal, 1, Integer::sum);
                }
            }
        }
        return kordused;
    }

    private String kuvatavKaal(Kaar kaar, Map<Integer, Integer> kaaludeKordused) {
        return kaaludeKordused.getOrDefault(kaar.kaal, 0) > 1 ? "1" : String.valueOf(kaar.kaal);
    }
}
