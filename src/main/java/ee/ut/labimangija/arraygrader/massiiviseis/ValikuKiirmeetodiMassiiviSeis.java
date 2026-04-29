package ee.ut.labimangija.arraygrader.massiiviseis;

public class ValikuKiirmeetodiMassiiviSeis extends MassiiviSeis {
    int vastusePiir; // esimese elemendi indeks, mis ei jää vähimate elementide hulka. võrdne enne piiri olevate elementide arvuga.

    public ValikuKiirmeetodiMassiiviSeis(int[] massiiv, Integer tööalaAlgusIndeks, Integer tööalaleJärgnevIndeks, int vastusePiir) {
        super(massiiv, tööalaAlgusIndeks, tööalaleJärgnevIndeks);
        if (vastusePiir <= 0 || vastusePiir > massiiv.length)
            throw new IllegalArgumentException("Valiku kiirmeetodi vastuse piir peab olema massiivi sees.");
        this.vastusePiir = vastusePiir;
    }

    public int getVastusePiir() {
        return vastusePiir;
    }

    @Override
    public ValikuKiirmeetodiMassiiviSeis teeKoopia() {
        return new ValikuKiirmeetodiMassiiviSeis(this.getMassiiv(), this.getTööalaAlgusIndeks(), this.getTööalaleJärgnevIndeks(), this.vastusePiir);
    }
}

