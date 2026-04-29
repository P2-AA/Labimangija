package ee.ut.labimangija.hashgrader.samm;

import ee.ut.labimangija.hashgrader.Läbimäng;

public interface Samm {
    boolean astu(Läbimäng läbimäng);

    boolean tagasi(Läbimäng läbimäng);

    boolean equals(Object o);

    String toString();
}

