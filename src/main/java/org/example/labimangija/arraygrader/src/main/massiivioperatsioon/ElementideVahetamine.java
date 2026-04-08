package org.example.labimangija.arraygrader.massiivioperatsioon;

import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;

public abstract class ElementideVahetamine extends Massiivioperatsioon {
    int vasakpoolseElemendiIndeks;
    int parempoolseElemendiIndeks;

    public ElementideVahetamine(int üheVahetatavaIndeks, int teiseVahetatavaIndeks, MassiiviSeis massiivEnneOperatsiooni) {
        super(massiivEnneOperatsiooni);
        if (üheVahetatavaIndeks == teiseVahetatavaIndeks) {
            throw new IllegalArgumentException("Vahetatavad elemendid peavad olema erinevad.");
        }
        if (üheVahetatavaIndeks < 0 || üheVahetatavaIndeks >=massiivEnneOperatsiooni.getMassiiv().length
                || teiseVahetatavaIndeks < 0 || teiseVahetatavaIndeks >= massiivEnneOperatsiooni.getMassiiv().length) {
            throw new IllegalArgumentException("Mõlema vahetatava indeksid peavad olema massiivi sees.");
        }
        this.vasakpoolseElemendiIndeks = Math.min(üheVahetatavaIndeks, teiseVahetatavaIndeks);
        this.parempoolseElemendiIndeks = Math.max(üheVahetatavaIndeks, teiseVahetatavaIndeks);
        teeVahetus();
    }

    private void teeVahetus() {
        int abi = getSeis().getMassiiv()[vasakpoolseElemendiIndeks];
        getSeis().getMassiiv()[vasakpoolseElemendiIndeks] = getSeis().getMassiiv()[parempoolseElemendiIndeks];
        getSeis().getMassiiv()[parempoolseElemendiIndeks] = abi;
    }

    @Override
    public String toString() {
        return "Elementide vahetus indeksitel " + vasakpoolseElemendiIndeks + " ja " + parempoolseElemendiIndeks + ". Uus massiiv: " + getSeis() + ".";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ElementideVahetamine elementideVahetamine)) {
            return false;
        }

        return this.vasakpoolseElemendiIndeks == elementideVahetamine.vasakpoolseElemendiIndeks
                && this.parempoolseElemendiIndeks == elementideVahetamine.parempoolseElemendiIndeks;
    }
}
