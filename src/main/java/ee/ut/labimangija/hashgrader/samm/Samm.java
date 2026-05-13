package ee.ut.labimangija.hashgrader.samm;

import ee.ut.labimangija.hashgrader.Läbimäng;

// Klassi implementatsioon põhineb peamiselt Karolin Konradi loodud lahendusel.
// Eeskujuks kasutatud töö: "Paisktabelialgoritmide läbimängu automaatse hindaja loomine", kättesaadav aadressil:
// https://thesis.cs.ut.ee/de177ee2-57a8-428a-b4b9-cac9982f5bd4

public interface Samm {
    boolean astu(Läbimäng läbimäng);

    boolean tagasi(Läbimäng läbimäng);

    boolean equals(Object o);

    String toString();
}

