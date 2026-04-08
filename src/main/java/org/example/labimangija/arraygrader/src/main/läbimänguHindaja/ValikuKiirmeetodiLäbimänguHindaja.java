package org.example.labimangija.arraygrader.labimanguhindaja;

import org.example.labimangija.arraygrader.massiivioperatsioon.LahkmeJärgiJaotamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

import java.util.List;

public class ValikuKiirmeetodiLäbimänguHindaja extends LäbimänguHindaja {
    @Override
    protected int leiaRaskusparameeter(List<Massiivioperatsioon> tehtudKäigud) {
        // valiku kiirmeetodi raskusparameeter on lahkme järgi jagamiste arv

        int raskusparameeter = 0;
        for (Massiivioperatsioon käik : tehtudKäigud) {
            if (käik instanceof LahkmeJärgiJaotamine) {
                raskusparameeter += 1;
            }
        }

        return raskusparameeter;
    }
}
