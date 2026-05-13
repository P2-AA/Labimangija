package ee.ut.labimangija.arraygrader.massiiviseis;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

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

