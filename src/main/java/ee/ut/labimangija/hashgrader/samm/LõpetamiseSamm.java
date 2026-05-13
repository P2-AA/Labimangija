package ee.ut.labimangija.hashgrader.samm;

import ee.ut.labimangija.hashgrader.Läbimäng;

// Klassi implementatsioon põhineb peamiselt Karolin Konradi loodud lahendusel.
// Eeskujuks kasutatud töö: "Paisktabelialgoritmide läbimängu automaatse hindaja loomine", kättesaadav aadressil:
// https://thesis.cs.ut.ee/de177ee2-57a8-428a-b4b9-cac9982f5bd4

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

