package ee.ut.labimangija.hashgrader.ylesanne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ee.ut.labimangija.hashgrader.Hinnang;
import ee.ut.labimangija.hashgrader.Läbimäng;
import ee.ut.labimangija.hashgrader.Paisktabel;
import ee.ut.labimangija.hashgrader.ResourceReader;
import ee.ut.labimangija.hashgrader.samm.EemaldamiseSamm;
import ee.ut.labimangija.hashgrader.samm.LõpetamiseSamm;
import ee.ut.labimangija.hashgrader.samm.PaisktabeliLoomiseSamm;
import ee.ut.labimangija.hashgrader.samm.Samm;
import ee.ut.labimangija.hashgrader.samm.SisestamiseSamm;

import static ee.ut.labimangija.hashgrader.Hindaja.Olek.EEMALDAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.LISAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.LÕPP;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.RASKE_LISAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.TABELI_LOOMINE;

// Klassi implementatsioon põhineb peamiselt Karolin Konradi loodud lahendusel.
// Eeskujuks kasutatud töö: "Paisktabelialgoritmide läbimängu automaatse hindaja loomine", kättesaadav aadressil:
// https://thesis.cs.ut.ee/de177ee2-57a8-428a-b4b9-cac9982f5bd4

public class KimbuYlesanne extends Ylesanne<Float> {
    private ArrayList<Float> sisend;
    private ArrayList<Float> algneSisend;
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
        return paiskfunktsioon(arv, tudengiMinElem, tudengiMaxElem, tudengiElementideArv);
    }

    private int paiskfunktsioon(float arv, float minElem, float maxElem, int elementideArv) {
        return (int) Math.floor((arv - minElem) / (maxElem - minElem) * elementideArv);
    }

    private boolean kasRasiOnPaisktabelis(int rasi, int paisktabeliPikkus) {
        return rasi >= 0 && rasi < paisktabeliPikkus;
    }

    private boolean kasSobivadPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv) {
        if (elementideArv <= 0 || maxElem <= minElem) {
            return false;
        }
        for (Float arv : algneSisend) {
            int rasi = paiskfunktsioon(arv, minElem, maxElem, elementideArv);
            if (!kasRasiOnPaisktabelis(rasi, elementideArv)) {
                return false;
            }
        }
        return true;
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

        algneSisend = new ArrayList<>(sisend);

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
        return new ArrayList<>(algneSisend);
    }

    @Override
    public void setPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv) {
        if (!kasSobivadPaisktabeliParameetrid(minElem, maxElem, elementideArv)) {
            throw new IllegalArgumentException(
                    "Sobimatu algseadistus. Sisesta a b m nii, et kõik elemendid jaotuksid ridadele 0 kuni m-1."
            );
        }
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
        return "Järjesta ahel kimbumeetodil: " + algneSisend;
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
            if (!kasRasiOnPaisktabelis(räsi, paisktabel.size())) {
                return new Hinnang(new SisestamiseSamm<Float>(0, räsi, 0), LISAMINE, samm, false);
            }
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
