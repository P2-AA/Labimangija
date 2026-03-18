package org.example.labimangija.hashgrader.ylesanne;

import java.io.IOException;
import java.util.ArrayList;
import org.example.labimangija.hashgrader.Hinnang;
import org.example.labimangija.hashgrader.Läbimäng;
import org.example.labimangija.hashgrader.Paisktabel;
import org.example.labimangija.hashgrader.samm.Samm;

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
