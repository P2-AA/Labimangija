package ee.ut.labimangija.hashgrader;

import java.util.ArrayList;
import java.util.Stack;

// Klassi implementatsioon põhineb peamiselt Karolin Konradi loodud lahendusel.
// Eeskujuks kasutatud töö: "Paisktabelialgoritmide läbimängu automaatse hindaja loomine", kättesaadav aadressil:
// https://thesis.cs.ut.ee/de177ee2-57a8-428a-b4b9-cac9982f5bd4

public class Hindaja {

    public enum Olek {
        RASKE_LISAMINE,
        LISAMINE,
        TABELI_LOOMINE,
        LÕPP,
        EEMALDAMINE,
        KUSTUTAMINE
    }

    public float arvutaHinne(Stack<Hinnang> läbimäng, ArrayList<Hinnang> õigeLäbimäng) {
        float punktideSumma = 0f;
        int maxPunktid = õigeLäbimäng.size();

        while (!läbimäng.isEmpty()) {
            if (läbimäng.pop().õige) {
                punktideSumma += 1;
            }
        }
        return punktideSumma / maxPunktid * 100.0f;
    }
}

