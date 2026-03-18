package org.example.labimangija.hashgrader.ylesanne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.example.labimangija.hashgrader.Hinnang;
import org.example.labimangija.hashgrader.Läbimäng;
import org.example.labimangija.hashgrader.Paisktabel;
import org.example.labimangija.hashgrader.ResourceReader;
import org.example.labimangija.hashgrader.samm.EemaldamiseSamm;
import org.example.labimangija.hashgrader.samm.LõpetamiseSamm;
import org.example.labimangija.hashgrader.samm.PaisktabeliLoomiseSamm;
import org.example.labimangija.hashgrader.samm.Samm;
import org.example.labimangija.hashgrader.samm.SisestamiseSamm;

import static org.example.labimangija.hashgrader.Hindaja.Olek.EEMALDAMINE;
import static org.example.labimangija.hashgrader.Hindaja.Olek.LISAMINE;
import static org.example.labimangija.hashgrader.Hindaja.Olek.LÕPP;
import static org.example.labimangija.hashgrader.Hindaja.Olek.RASKE_LISAMINE;
import static org.example.labimangija.hashgrader.Hindaja.Olek.TABELI_LOOMINE;

public class KimbuYlesanne extends Ylesanne<Float> {
    private ArrayList<Float> sisend;
    private int kompesamm;
    private int elementideArv;
    private float minElem;
    private float maxElem;
    private boolean sisestamine;
    private int tudengiElementideArv;
    private float tudengiMinElem;
    private float tudengiMaxElem;

    public KimbuYlesanne(String failiTee) throws IOException {
        loeSisend(failiTee);
    }

    public int paiskfunktsioon(float arv) {
        return (int) Math.floor((arv - tudengiMinElem) / (tudengiMaxElem - tudengiMinElem) * tudengiElementideArv);
    }

    @Override
    public void loeSisend(String failiTee) throws IOException {
        List<String> read = ResourceReader.readLines(failiTee);
        String rida = read.get(new Random().nextInt(read.size()));

        sisend = new ArrayList<>();
        minElem = Float.MAX_VALUE;
        maxElem = Float.MIN_VALUE;

        for (String s : rida.split(" ")) {
            float arv = Float.parseFloat(s.replaceAll("[\\[\\]]", ""));
            if (arv < minElem) {
                minElem = arv;
            }
            if (maxElem < arv) {
                maxElem = arv;
            }
            sisend.add(arv);
        }

        maxElem = (float) Math.ceil(maxElem);
        setPaisktabeliParameetrid(minElem, maxElem, sisend.size());
        kompesamm = 0;
    }

    @Override
    public Paisktabel<Float> getPaisktabel() {
        return new Paisktabel<>(kompesamm);
    }

    @Override
    public ArrayList<Float> getAbijärjend() {
        return new ArrayList<>(sisend);
    }

    @Override
    public void setPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv) {
        this.tudengiMinElem = minElem;
        this.tudengiMaxElem = maxElem;
        this.tudengiElementideArv = elementideArv;
    }

    @Override
    public ArrayList<Hinnang> leiaÕigeLäbimäng() {
        ArrayList<Hinnang> õigeLäbimäng = new ArrayList<>();
        elementideArv = sisend.size();

        Paisktabel<Float> p = new Paisktabel<>(0, elementideArv);
        õigeLäbimäng.add(new Hinnang(new PaisktabeliLoomiseSamm(minElem, maxElem, elementideArv), TABELI_LOOMINE, null, true));

        for (Float arv : sisend) {
            int räsi = paiskfunktsioon(arv);
            int i;
            for (i = 0; i < p.get(räsi).size(); i++) {
                if (arv <= p.get(räsi, i)) {
                    break;
                }
            }

            if (p.get(räsi).size() > 0) {
                õigeLäbimäng.add(new Hinnang(new SisestamiseSamm<Float>(0, räsi, i), RASKE_LISAMINE, null, true));
            } else {
                õigeLäbimäng.add(new Hinnang(new SisestamiseSamm<Float>(0, räsi, i), LISAMINE, null, true));
            }

            p.sisesta(räsi, i, arv);
        }

        sisend.clear();
        for (int i = 0; i < p.size(); i++) {
            while (p.get(i).size() > 0) {
                sisend.add(p.get(i, 0));
                p.eemalda(i, 0);
                õigeLäbimäng.add(new Hinnang(new EemaldamiseSamm<Float>(sisend.size() - 1, i, 0), EEMALDAMINE, null, true));
            }
        }

        õigeLäbimäng.add(new Hinnang(new LõpetamiseSamm(), LÕPP, null, true));
        sisestamine = true;
        return õigeLäbimäng;
    }

    @Override
    public String ylesandeKirjeldus() {
        return "Järjestada ahel kimbumeetodil: " + sisend;
    }

    @Override
    public void astu(Läbimäng<Float> läbimäng, Hinnang hinnang) {
        if (läbimäng.getAbijärjend().size() == 0 && sisestamine) {
            sisestamine = false;
        }
    }

    @Override
    public void tagasi(Läbimäng<Float> läbimäng, Hinnang hinnang) {
        if (läbimäng.getAbijärjend().size() == 1 && !sisestamine
                && (hinnang.olek == LISAMINE || hinnang.olek == RASKE_LISAMINE)) {
            sisestamine = true;
        }
    }

    @Override
    public Hinnang hindaSammu(Samm samm, ArrayList<Float> abijärjend, Paisktabel<Float> paisktabel) {
        Samm õigeSamm = new LõpetamiseSamm();

        if (paisktabel.size() == 0) {
            õigeSamm = new PaisktabeliLoomiseSamm(minElem, maxElem, elementideArv);
            return new Hinnang(õigeSamm, TABELI_LOOMINE, samm, õigeSamm.equals(samm));
        }

        if (abijärjend.size() > 0 && sisestamine) {
            float arv = abijärjend.get(0);
            int räsi = paiskfunktsioon(arv);
            int i;
            for (i = 0; i < paisktabel.get(räsi).size(); i++) {
                if (arv <= paisktabel.get(räsi, i)) {
                    break;
                }
            }
            õigeSamm = new SisestamiseSamm<Float>(0, räsi, i);

            if (paisktabel.get(räsi).size() == 0) {
                return new Hinnang(õigeSamm, LISAMINE, samm, õigeSamm.equals(samm));
            }
            return new Hinnang(õigeSamm, RASKE_LISAMINE, samm, õigeSamm.equals(samm));
        }

        for (int i = 0; i < paisktabel.size(); i++) {
            if (paisktabel.get(i).size() > 0) {
                õigeSamm = new EemaldamiseSamm<Float>(abijärjend.size(), i, 0);
                return new Hinnang(õigeSamm, EEMALDAMINE, samm, õigeSamm.equals(samm));
            }
        }

        return new Hinnang(õigeSamm, LÕPP, samm, õigeSamm.equals(samm));
    }
}
