package org.example.labimangija.hashgrader;

import org.example.labimangija.hashgrader.samm.Samm;

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
