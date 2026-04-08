package org.example.labimangija.arraygrader.tests;

import org.example.labimangija.arraygrader.labimanguhindaja.Hindamistulemus;
import org.example.labimangija.arraygrader.labimanguhindaja.LäbimänguHindaja;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.example.labimangija.arraygrader.MassiiviTööriistad.kopeeriKäigudJajätkaLäbimängu;
import static org.example.labimangija.arraygrader.MassiiviTööriistad.kasÕigeTulemus;
import static org.junit.jupiter.api.Assertions.*;

public abstract class Testid {
    Random random = new Random(3);
    LäbimänguHindaja läbimänguHindaja;
    int katseteKordusi = 100;
    int massiivisElemente = 6;
    int maxJuhuslikVäärtus = 20;

    abstract LäbimänguAlustamine uueLäbimänguAlustamiseOperatsioon();

    abstract List<Massiivioperatsioon> kõikvõimalikudKäigud(MassiiviSeis massiiviSeis);

    @org.junit.jupiter.api.Test
    void korrektneLäbimängTest() {
        for (int i = 0; i < katseteKordusi; i++) {
            List<Massiivioperatsioon> käigud = new ArrayList<>();

            Massiivioperatsioon viimaneKäik = uueLäbimänguAlustamiseOperatsioon();
            käigud = kopeeriKäigudJajätkaLäbimängu(käigud, viimaneKäik);

            Hindamistulemus hindamistulemus = läbimänguHindaja.hinda(käigud);
            assertEquals(0, hindamistulemus.getValedeKäikudeArv());
            assertEquals(1, hindamistulemus.arvutaPunktid());

            MassiiviSeis algoritmiTulemus = käigud.get(käigud.size() - 1).getSeis();

            assertTrue(kasÕigeTulemus(algoritmiTulemus), "Viga massiivi " + käigud.get(0).getSeis() + " ümber järjestamisel");
        }
    }

    @org.junit.jupiter.api.Test
    void läbimängPealeValetKäikuTest() {
        /*
        Vaadatakse läbi kõik võimalikud juhud, kus läbimängul on 1 vale käik.
        Massiivil hakatakse järjest õigeid käike tegema. Peale iga õiget käiku vaadatakse läbi kõik variandid, kuidas selles
        seisus saaks vale käigu teha. Kontrollitakse, kas kui peale vale käiku tehakse edasi ainult õigeid käike, jõutakse
        oodatud seisuni (kui oluline viga leidub, siis on oodatud seisuks valesti järjestatud massiiv, ning kui olulist
        viga pole, siis on oodatud seisuks õigesti järjestatud massiiv). Kontrollitakse, kas kõikvõimalike käikude seas on
        täpselt üks õige käik. Lõpuks kontrollitakse, kas kõikide juhtude seas,
        kus läbimängul on täpselt 1 viga, esinevad vähemalt üks kord oluline viga; mitteoluline viga; lahendus, millel on
        algse ülesande raskusparameeter; lahendus, millel on erinev raskusparameeter; lahendus, mis sai üle 0 punkti.
         */
        for (int i = 0; i < katseteKordusi; i++) {
            boolean esinebOlulineViga = false;
            boolean esinebMitteolulineViga = false;
            boolean esinebLahendusOodatudRaskusparameetriga = false;
            boolean esinebLahendusMitteOodatudRaskusparameetriga = false;
            boolean esinebLahendusMisSaiPunkte = false;

            List<Massiivioperatsioon> õigedKäigud = new ArrayList<>();

            Massiivioperatsioon praeguneKäik = uueLäbimänguAlustamiseOperatsioon();
            õigedKäigud.add(praeguneKäik);
            Massiivioperatsioon järgmineÕigeKäik;
            // teen järjest õigeid käike
            while (!(praeguneKäik instanceof LäbimänguLõpetamine)) {
                järgmineÕigeKäik = praeguneKäik.järgmineÕigeKäik();
                List<Massiivioperatsioon> võimalikudKäigud = kõikvõimalikudKäigud(praeguneKäik.getSeis());

                // peale iga õige käigu tegemist vaatan läbi kõik variandid, kus teen nüüd vale käigu ja jälle edasi õigesti
                int õigeidKäikeVõimalikeKäikudeSeas = 0; // kõikide võimalike käikude seas peaks olema ainult üks õige käik
                for (Massiivioperatsioon võimalikKäik : võimalikudKäigud) {
                    if (võimalikKäik.equals(järgmineÕigeKäik)) {
                        õigeidKäikeVõimalikeKäikudeSeas++;
                        continue;
                    }

                    List<Massiivioperatsioon> lõpuniJärjestamine1Veaga = kopeeriKäigudJajätkaLäbimängu(õigedKäigud, võimalikKäik);
                    // kõik käigud on tehtud

                    // kontrollin kas tulemus on korrektne

                    Hindamistulemus hindamistulemus = läbimänguHindaja.hinda(lõpuniJärjestamine1Veaga);

                    assertEquals(1, hindamistulemus.getValedeKäikudeArv());
                    assertTrue(hindamistulemus.arvutaPunktid() < 1);

                    MassiiviSeis algoritmiTulemus1Veaga = lõpuniJärjestamine1Veaga.get(lõpuniJärjestamine1Veaga.size() - 1).getSeis();
                    if (hindamistulemus.getOluliseVeaIndeks() == null) {
                        esinebMitteolulineViga = true;
                        assertTrue(kasÕigeTulemus(algoritmiTulemus1Veaga),
                                "Viga massiivi " + lõpuniJärjestamine1Veaga.get(0).getSeis() + " ümber järjestamisel: "
                                        + Arrays.toString(algoritmiTulemus1Veaga.getMassiiv()) + " on vale tulemus.");
                    } else {
                        esinebOlulineViga = true;
                        assertFalse(kasÕigeTulemus(algoritmiTulemus1Veaga), "Massiivi " + lõpuniJärjestamine1Veaga.get(0).getSeis()
                                + " ümber järjestamisel saadi õige tulemus, kuigi kirjas on oluline viga.");
                    }

                    if (hindamistulemus.getRaskusparameeter() == hindamistulemus.getOodatudRaskusparameeter()) {
                        esinebLahendusOodatudRaskusparameetriga = true;
                    } else {
                        esinebLahendusMitteOodatudRaskusparameetriga = true;
                    }

                    esinebLahendusMisSaiPunkte = esinebLahendusMisSaiPunkte || hindamistulemus.arvutaPunktid() != 0;
                }
                assertEquals(1, õigeidKäikeVõimalikeKäikudeSeas);

                // jätkan algse käikude listi õigesti täitmist
                praeguneKäik = järgmineÕigeKäik;
                õigedKäigud.add(praeguneKäik);
            }

            // kontroll, kas ikka on võimalik erinevaid vastuseid saada
            assertTrue(esinebOlulineViga);
            assertTrue(esinebMitteolulineViga);
            assertTrue(esinebLahendusOodatudRaskusparameetriga);
            assertTrue(esinebLahendusMitteOodatudRaskusparameetriga);
            assertTrue(esinebLahendusMisSaiPunkte);
        }
    }

    MassiiviSeis looUusMassiiviSeis() {
        int[] massiiv = new int[massiivisElemente];
        for (int j = 0; j < massiiv.length; j++) {
            massiiv[j] = random.nextInt(maxJuhuslikVäärtus);
        }
        return new MassiiviSeis(massiiv, null, null);
    }

}
