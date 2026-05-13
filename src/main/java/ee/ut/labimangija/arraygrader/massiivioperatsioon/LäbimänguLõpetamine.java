package ee.ut.labimangija.arraygrader.massiivioperatsioon;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class LäbimänguLõpetamine extends Massiivioperatsioon {
    public LäbimänguLõpetamine(MassiiviSeis massiivEnneOperatsiooni) {
        super(massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        return null; // peale läbimängu lõpetamist ei ole kunagi järgmist õiget käiku
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        return false;
    }

    @Override
    public String toString() {
        return "Läbimängu lõpetamine.";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return o instanceof LäbimänguLõpetamine;
    }
}

