package ee.ut.labimangija.algorithmgrader.Kahendpuu;

// Klassi implementatsioon põhineb peamiselt Markus Michelise loodud lahendusel.
// Eeskujuks kasutatud töö: "kahendpuu- ja kuhjaalgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/e07a9cf2-900d-4db8-9d05-5c24d48e424c

public class Tipp {
    public int väärtus;
    public Tipp vasak;
    public Tipp parem;
    public VisuaalneTipp visuaalneTipp;

    // metsa kuvamiseks
    public int tase = 0;

    //Elemendi indeks kuhjas
    public int indeks;
    public Tipp(int väärtus, Tipp v, Tipp p) {
        this.väärtus = väärtus;
        this.vasak = v;
        this.parem = p;
    }
    public Tipp(int väärtus, Tipp v, Tipp p, int indeks) {
        this.väärtus = väärtus;
        this.vasak = v;
        this.parem = p;
        this.indeks = indeks;
    }

    public Tipp(int väärtus) {
        this.väärtus = väärtus;
        this.vasak = null;
        this.parem = null;

    }
    public int getVäärtus() {
        return väärtus;
    }

    @Override
    public String toString() {
        return ""+väärtus;
    }
}
