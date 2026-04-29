package ee.ut.labimangija.arraygrader.labimanguhindaja;

import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiElementideVahetamine;

import java.util.List;

public class ValikumeetodiLäbimänguHindaja extends LäbimänguHindaja {
    @Override
    protected int leiaRaskusparameeter(List<Massiivioperatsioon> tehtudKäigud) {
        // valikumeetodi raskusparameeter on tehtavate vahetuste arv

        int raskusparameeter = 0;
        for (Massiivioperatsioon käik : tehtudKäigud) {
            if (käik instanceof ValikumeetodiElementideVahetamine) {
                raskusparameeter += 1;
            }
        }

        return raskusparameeter;
    }
}

