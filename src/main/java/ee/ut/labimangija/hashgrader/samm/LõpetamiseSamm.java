package ee.ut.labimangija.hashgrader.samm;

import ee.ut.labimangija.hashgrader.Läbimäng;

public class LõpetamiseSamm implements Samm {

    @Override
    public boolean astu(Läbimäng läbimäng) {
        läbimäng.lõpeta();
        return true;
    }

    @Override
    public boolean tagasi(Läbimäng läbimäng) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override
    public String toString() {
        return "algoritm lõpetab";
    }
}

