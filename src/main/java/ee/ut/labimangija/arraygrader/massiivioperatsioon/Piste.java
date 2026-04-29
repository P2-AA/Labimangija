package ee.ut.labimangija.arraygrader.massiivioperatsioon;


import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;

public abstract class Piste extends Massiivioperatsioon {
    private final int pisteAlgusIndeks; // kus on element enne pistet
    private final int pisteLõpuIndeks; // kus on element peale pistet

    public Piste(int pisteAlgusIndeks, int pisteLõpuIndeks, MassiiviSeis massiivEnnePistet) {
        super(massiivEnnePistet);
        if (pisteAlgusIndeks < 0 || pisteAlgusIndeks >= massiivEnnePistet.getMassiiv().length
                || pisteLõpuIndeks < 0 || pisteLõpuIndeks >= massiivEnnePistet.getMassiiv().length) {
            throw new IllegalArgumentException("Piste algus- ja lõpp-punkt peavad olema massiivi sees.");
        }
        if (pisteAlgusIndeks == pisteLõpuIndeks) {
            throw new IllegalArgumentException("Piste algus- ja lõpp-punkt peavad olema erinevad.");
        }
        this.pisteAlgusIndeks = pisteAlgusIndeks;
        this.pisteLõpuIndeks = pisteLõpuIndeks;
        teePiste();
    }

    public int getPisteAlgusIndeks() {
        return pisteAlgusIndeks;
    }

    public int getPisteLõpuIndeks() {
        return pisteLõpuIndeks;
    }

    private void teePiste() {
        int samm = 1;
        if (pisteAlgusIndeks > pisteLõpuIndeks) samm = -1;

        int[] massiiv = getSeis().getMassiiv();
        int tõstetavElement = massiiv[pisteAlgusIndeks];
        int praeguneIndeks = pisteAlgusIndeks;

        while (praeguneIndeks != pisteLõpuIndeks) {
            massiiv[praeguneIndeks] = massiiv[praeguneIndeks + samm];
            praeguneIndeks += samm;
        }
        massiiv[pisteLõpuIndeks] = tõstetavElement;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Piste piste)) {
            return false;
        }

        return this.pisteAlgusIndeks == piste.pisteAlgusIndeks
                && this.pisteLõpuIndeks == piste.pisteLõpuIndeks;
    }

    @Override
    public String toString() {
        return "Piste indeksilt " + pisteAlgusIndeks + " indeksile " + pisteLõpuIndeks + ". Massiivi seis peale pistet: " + getSeis() + ".";
    }
}

