package ee.ut.labimangija.graphgrader.Graaf;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7
public class Kaar {
    public Tipp algus;
    public Tipp lopp;
    public int kaal;
    public Arrow arrow;

    public Kaar(Tipp algus, Tipp lopp, int kaal) {
        this.algus = algus;
        this.lopp = lopp;
        this.kaal = kaal;
    }

    public Kaar(Tipp algus, Tipp lopp) {
        this.algus = algus;
        this.lopp = lopp;
    }

    public String toString() {
        return algus.tähis + "->" + lopp.tähis + ": " + kaal;
    }
}


