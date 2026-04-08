package org.example.labimangija.arraygrader.massiivioperatsioon;

import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;

import java.util.Arrays;

public abstract class LahkmeJärgiJaotamine extends Massiivioperatsioon {
    int lahkmeJärgiJaotamisePiiristJärgnevIndeks; // piir, mis eraldab lahkmest suuremad ja võrdsed elemendid väiksematest

    public LahkmeJärgiJaotamine(MassiiviSeis massiivEnneOperatsiooni, int lahkmeJärgiJaotamisePiiristJärgnevIndeks) {
        super(massiivEnneOperatsiooni);
        this.lahkmeJärgiJaotamisePiiristJärgnevIndeks = lahkmeJärgiJaotamisePiiristJärgnevIndeks;
    }

    public int getLahkmeJärgiJaotamisePiiristJärgnevIndeks() {
        return lahkmeJärgiJaotamisePiiristJärgnevIndeks;
    }

    @Override
    public String toString() {
        if (!(getSeis() instanceof ValikuKiirmeetodiMassiiviSeis massiiviSeis)) {
            throw new IllegalArgumentException("Massiivi seis peab olema ValikuKiirmeetodiMassiiviSeis-tüüpi.");
        }
        return "Lahkme järgi jaotamine. Massiivi seis peale lahkme järgi jaotamist: " + massiiviSeis + ", lahe on indeksil "
                + lahkmeJärgiJaotamisePiiristJärgnevIndeks + ", vastuse piir on indeksil " + massiiviSeis.getVastusePiir() + ".";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof LahkmeJärgiJaotamine lahkmeJärgiJaotamine)) {
            return false;
        }

        return this.lahkmeJärgiJaotamisePiiristJärgnevIndeks == lahkmeJärgiJaotamine.getLahkmeJärgiJaotamisePiiristJärgnevIndeks()
                && Arrays.equals(this.getSeis().getMassiiv(), lahkmeJärgiJaotamine.getSeis().getMassiiv());
    }
}
