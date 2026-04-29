package ee.ut.labimangija.arraygrader.labimanguhindaja;

import ee.ut.labimangija.arraygrader.massiivioperatsioon.LahkmeJärgiJaotamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

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

