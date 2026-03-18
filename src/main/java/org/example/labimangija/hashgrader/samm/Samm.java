package org.example.labimangija.hashgrader.samm;

import org.example.labimangija.hashgrader.Läbimäng;

public interface Samm {
    boolean astu(Läbimäng läbimäng);

    boolean tagasi(Läbimäng läbimäng);

    boolean equals(Object o);

    String toString();
}
