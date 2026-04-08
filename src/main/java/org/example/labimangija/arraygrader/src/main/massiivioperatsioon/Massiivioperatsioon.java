package org.example.labimangija.arraygrader.massiivioperatsioon;

import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;

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
