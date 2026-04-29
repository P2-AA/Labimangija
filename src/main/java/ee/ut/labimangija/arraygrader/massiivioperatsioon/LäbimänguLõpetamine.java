package ee.ut.labimangija.arraygrader.massiivioperatsioon;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;

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

