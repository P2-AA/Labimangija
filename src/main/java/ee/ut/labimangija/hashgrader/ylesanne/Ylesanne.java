package ee.ut.labimangija.hashgrader.ylesanne;

import java.io.IOException;
import java.util.ArrayList;
import ee.ut.labimangija.hashgrader.Hinnang;
import ee.ut.labimangija.hashgrader.Läbimäng;
import ee.ut.labimangija.hashgrader.Paisktabel;
import ee.ut.labimangija.hashgrader.samm.Samm;

public abstract class Ylesanne<T> {

    public int paiskfunktsioon(int arv, Paisktabel<T> paisktabel) {
        return arv % paisktabel.size();
    }

    public abstract void loeSisend(String failiTee) throws IOException;

    public abstract Paisktabel<T> getPaisktabel();

    public abstract ArrayList<T> getAbijärjend();

    public abstract void setPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv);

    public abstract ArrayList<Hinnang> leiaÕigeLäbimäng();

    public abstract String ylesandeKirjeldus();

    public abstract void astu(Läbimäng<T> läbimäng, Hinnang hinnang);

    public abstract void tagasi(Läbimäng<T> läbimäng, Hinnang hinnang);

    public abstract Hinnang hindaSammu(Samm samm, ArrayList<T> abijärjend, Paisktabel<T> paisktabel);
}

