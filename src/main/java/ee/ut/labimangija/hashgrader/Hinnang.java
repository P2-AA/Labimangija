package ee.ut.labimangija.hashgrader;

import ee.ut.labimangija.hashgrader.samm.Samm;

// Klassi implementatsioon põhineb peamiselt Karolin Konradi loodud lahendusel.
// Eeskujuks kasutatud töö: "Paisktabelialgoritmide läbimängu automaatse hindaja loomine", kättesaadav aadressil:
// https://thesis.cs.ut.ee/de177ee2-57a8-428a-b4b9-cac9982f5bd4

public class Hinnang {
    public final Samm õigeSamm;
    public final Hindaja.Olek olek;
    public final Samm tudengiSamm;
    public final boolean õige;

    public Hinnang(Samm õigeSamm, Hindaja.Olek olek, Samm tudengiSamm, boolean õige) {
        this.õigeSamm = õigeSamm;
        this.olek = olek;
        this.tudengiSamm = tudengiSamm;
        this.õige = õige;
    }

    @Override
    public String toString() {
        if (õige) {
            return "> " + tudengiSamm + "\n" + olek;
        }
        return "*VIGA*\n> " + tudengiSamm + "\n"
                + "õige -> " + õigeSamm + "\n"
                + olek;
    }
}

