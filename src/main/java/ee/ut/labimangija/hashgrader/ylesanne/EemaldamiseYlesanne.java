package ee.ut.labimangija.hashgrader.ylesanne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import ee.ut.labimangija.hashgrader.Hinnang;
import ee.ut.labimangija.hashgrader.Läbimäng;
import ee.ut.labimangija.hashgrader.Paisktabel;
import ee.ut.labimangija.hashgrader.ResourceReader;
import ee.ut.labimangija.hashgrader.samm.EemaldamiseSamm;
import ee.ut.labimangija.hashgrader.samm.LõpetamiseSamm;
import ee.ut.labimangija.hashgrader.samm.Samm;
import ee.ut.labimangija.hashgrader.samm.SisestamiseSamm;

import static ee.ut.labimangija.hashgrader.Hindaja.Olek.EEMALDAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.KUSTUTAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.LISAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.LÕPP;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.RASKE_LISAMINE;

public class EemaldamiseYlesanne extends Ylesanne<Integer> {
    private ArrayList<Integer> sisend;
    private int eemaldatav;
    private int eemaldatavaRäsi;
    private HashMap<Integer, Integer> kompejadaAlgsedIndeksid;
    private int järg;
    private int kompesamm;
    private int ridadeArv;

    public EemaldamiseYlesanne(String failiTee) throws IOException {
        loeSisend(failiTee);
    }

    @Override
    public void loeSisend(String failiTee) throws IOException {
        List<String> read = ResourceReader.readLines(failiTee);
        String rida = read.get(new Random().nextInt(read.size()));

        sisend = new ArrayList<>();
        String[] osad = rida.split(" ");
        int algusIndeks = 0;
        if (osad.length > 0 && osad[0].startsWith("m=")) {
            ridadeArv = Integer.parseInt(osad[0].substring(2));
            algusIndeks = 1;
        }
        for (int i = algusIndeks; i < osad.length; i++) {
            String s = osad[i];
            if (s.contains("*")) {
                eemaldatav = Integer.parseInt(s.replaceAll("[\\[*\\]]", ""));
            }
            sisend.add(Integer.parseInt(s.replaceAll("[\\[*\\]]", "")));
        }
        if (ridadeArv <= 0) {
            ridadeArv = sisend.size();
        }
        kompesamm = 1;
    }

    @Override
    public Paisktabel<Integer> getPaisktabel() {
        Paisktabel<Integer> paisktabel = new Paisktabel<>(kompesamm, ridadeArv);
        for (Integer arv : sisend) {
            paisktabel.sisesta(paisktabel.leiaVabaKoht(paiskfunktsioon(arv, paisktabel)), 0, arv);
        }
        return paisktabel;
    }

    @Override
    public ArrayList<Integer> getAbijärjend() {
        return new ArrayList<>();
    }

    @Override
    public void setPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv) {
    }

    @Override
    public ArrayList<Hinnang> leiaÕigeLäbimäng() {
        Paisktabel<Integer> p = getPaisktabel();
        kompejadaAlgsedIndeksid = new HashMap<>();
        ArrayList<Hinnang> õigeLäbimäng = new ArrayList<>();

        eemaldatavaRäsi = p.leiaAsukoht(eemaldatav, paiskfunktsioon(eemaldatav, p));
        p.eemalda(eemaldatavaRäsi, 0);
        õigeLäbimäng.add(new Hinnang(new EemaldamiseSamm<Integer>(0, eemaldatavaRäsi, 0), KUSTUTAMINE, null, true));

        int i = eemaldatavaRäsi;
        while (true) {
            i++;
            if (i >= p.size()) {
                i = 0;
            }
            if (i == eemaldatavaRäsi || p.get(i, 0) == null) {
                break;
            }

            int arv = p.get(i, 0);
            kompejadaAlgsedIndeksid.put(arv, i);
            p.eemalda(i, 0);
            õigeLäbimäng.add(new Hinnang(new EemaldamiseSamm<Integer>(0, i, 0), EEMALDAMINE, null, true));

            int uusKoht = p.leiaVabaKoht(paiskfunktsioon(arv, p));
            if (uusKoht != i) {
                õigeLäbimäng.add(new Hinnang(new SisestamiseSamm<Integer>(0, uusKoht, 0), RASKE_LISAMINE, null, true));
            } else {
                õigeLäbimäng.add(new Hinnang(new SisestamiseSamm<Integer>(0, uusKoht, 0), LISAMINE, null, true));
            }
            p.sisesta(uusKoht, 0, arv);
        }

        õigeLäbimäng.add(new Hinnang(new LõpetamiseSamm(), LÕPP, null, true));
        järg = eemaldatavaRäsi;
        return õigeLäbimäng;
    }

    @Override
    public String ylesandeKirjeldus() {
        return "Olgu lahtise adresseerimisega paisktabelil jääkpaiskamine, kompesamm " + kompesamm + " ja ridu "
                + ridadeArv + ".\nEemalda lahtise adresseerimiesega paisktabelist " + eemaldatav;
    }

    @Override
    public void astu(Läbimäng<Integer> läbimäng, Hinnang hinnang) {
        if ((hinnang.olek == EEMALDAMINE || hinnang.olek == KUSTUTAMINE) && hinnang.õige) {
            järg++;
        }
    }

    @Override
    public void tagasi(Läbimäng<Integer> läbimäng, Hinnang hinnang) {
        if ((hinnang.olek == EEMALDAMINE || hinnang.olek == KUSTUTAMINE) && hinnang.õige) {
            järg--;
        }
    }

    @Override
    public Hinnang hindaSammu(Samm samm, ArrayList<Integer> abijärjend, Paisktabel<Integer> paisktabel) {
        Samm õigeSamm = new LõpetamiseSamm();

        if (abijärjend.size() == 0) {
            int eemaldatavaVõti = paisktabel.leiaAsukoht(eemaldatav, paiskfunktsioon(eemaldatav, paisktabel));
            õigeSamm = new EemaldamiseSamm<Integer>(0, eemaldatavaVõti, 0);
            return new Hinnang(õigeSamm, KUSTUTAMINE, samm, õigeSamm.equals(samm));
        }

        if (abijärjend.size() > 1) {
            int arv = abijärjend.get(0);
            int räsi = paiskfunktsioon(arv, paisktabel);
            int vabaRäsi = paisktabel.leiaVabaKoht(räsi);
            õigeSamm = new SisestamiseSamm<Integer>(0, vabaRäsi, 0);

            if (vabaRäsi != kompejadaAlgsedIndeksid.get(arv)) {
                return new Hinnang(õigeSamm, RASKE_LISAMINE, samm, õigeSamm.equals(samm));
            }
            return new Hinnang(õigeSamm, LISAMINE, samm, õigeSamm.equals(samm));
        }

        if (järg >= paisktabel.size()) {
            järg = 0;
        }
        if (järg == eemaldatavaRäsi || paisktabel.get(järg).size() == 0) {
            return new Hinnang(õigeSamm, LÕPP, samm, õigeSamm.equals(samm));
        }

        õigeSamm = new EemaldamiseSamm<Integer>(0, järg, 0);
        return new Hinnang(õigeSamm, EEMALDAMINE, samm, õigeSamm.equals(samm));
    }
}

