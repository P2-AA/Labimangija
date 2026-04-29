package ee.ut.labimangija.hashgrader.ylesanne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ee.ut.labimangija.hashgrader.Hinnang;
import ee.ut.labimangija.hashgrader.Läbimäng;
import ee.ut.labimangija.hashgrader.Paisktabel;
import ee.ut.labimangija.hashgrader.ResourceReader;
import ee.ut.labimangija.hashgrader.samm.LõpetamiseSamm;
import ee.ut.labimangija.hashgrader.samm.Samm;
import ee.ut.labimangija.hashgrader.samm.SisestamiseSamm;

import static ee.ut.labimangija.hashgrader.Hindaja.Olek.LISAMINE;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.LÕPP;
import static ee.ut.labimangija.hashgrader.Hindaja.Olek.RASKE_LISAMINE;

public class LisamiseYlesanne extends Ylesanne<Integer> {

    private ArrayList<Integer> sisend;
    private int kompesamm;

    public LisamiseYlesanne(String failiTee) throws IOException {
        loeSisend(failiTee);
    }

    @Override
    public void loeSisend(String failiTee) throws IOException {
        List<String> read = ResourceReader.readLines(failiTee);
        String rida = read.get(new Random().nextInt(read.size()));

        sisend = new ArrayList<>();
        for (String s : rida.split(" ")) {
            sisend.add(Integer.parseInt(s.replaceAll("[\\[*\\]]", "")));
        }
        kompesamm = 1;
    }

    @Override
    public Paisktabel<Integer> getPaisktabel() {
        return new Paisktabel<>(kompesamm, sisend.size());
    }

    @Override
    public ArrayList<Integer> getAbijärjend() {
        return new ArrayList<>(sisend);
    }

    @Override
    public void setPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv) {
    }

    @Override
    public ArrayList<Hinnang> leiaÕigeLäbimäng() {
        Paisktabel<Integer> p = getPaisktabel();
        ArrayList<Hinnang> õigeLäbimäng = new ArrayList<>();

        for (Integer arv : sisend) {
            int räsi = paiskfunktsioon(arv, p);
            int koht = p.leiaVabaKoht(räsi);

            if (räsi != koht) {
                õigeLäbimäng.add(new Hinnang(new SisestamiseSamm<Integer>(0, koht, 0), RASKE_LISAMINE, null, true));
            } else {
                õigeLäbimäng.add(new Hinnang(new SisestamiseSamm<Integer>(0, koht, 0), LISAMINE, null, true));
            }

            p.sisesta(koht, 0, arv);
        }

        õigeLäbimäng.add(new Hinnang(new LõpetamiseSamm(), LÕPP, null, true));
        return õigeLäbimäng;
    }

    @Override
    public String ylesandeKirjeldus() {
        return "Olgu lahtise adresseerimisega paisktabelil jääkpaiskamine, kompesamm " + kompesamm + " ja ridu "
                + sisend.size() + ".\nLisa paisktabelisse samas järjekorras järgmised elemendid: " + sisend;
    }

    @Override
    public void astu(Läbimäng<Integer> läbimäng, Hinnang hinnang) {
    }

    @Override
    public void tagasi(Läbimäng<Integer> läbimäng, Hinnang hinnang) {
    }

    @Override
    public Hinnang hindaSammu(Samm samm, ArrayList<Integer> abijärjend, Paisktabel<Integer> paisktabel) {
        Samm õigeSamm = new LõpetamiseSamm();

        if (abijärjend.size() > 0) {
            int arv = abijärjend.get(0);
            int räsi = paiskfunktsioon(arv, paisktabel);
            int vabaRäsi = paisktabel.leiaVabaKoht(räsi);
            õigeSamm = new SisestamiseSamm<Integer>(0, vabaRäsi, 0);

            if (vabaRäsi == räsi) {
                return new Hinnang(õigeSamm, LISAMINE, samm, õigeSamm.equals(samm));
            }
            return new Hinnang(õigeSamm, RASKE_LISAMINE, samm, õigeSamm.equals(samm));
        }

        return new Hinnang(õigeSamm, LÕPP, samm, õigeSamm.equals(samm));
    }
}

