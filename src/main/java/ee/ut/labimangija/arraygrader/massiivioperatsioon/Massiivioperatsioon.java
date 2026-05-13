package ee.ut.labimangija.arraygrader.massiivioperatsioon;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public abstract class Massiivioperatsioon {
    MassiiviSeis seis; // massiivi seis peale operatsiooni

    public Massiivioperatsioon(MassiiviSeis massiivEnneOperatsiooni) {
        this.setSeis(massiivEnneOperatsiooni.teeKoopia());
    }

    public MassiiviSeis getSeis() {
        return seis;
    }

    public void setSeis(MassiiviSeis seis) {
        this.seis = seis;
    }

    public abstract boolean läbimänguOnVõimalikJätkata(); // kas läbimängu on võimalik peale praegust operatsiooni jätkata
    public abstract Massiivioperatsioon järgmineÕigeKäik(); // kehtib ainult juhul, kui läbimängu on võimalik jätkata

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object o);
}

