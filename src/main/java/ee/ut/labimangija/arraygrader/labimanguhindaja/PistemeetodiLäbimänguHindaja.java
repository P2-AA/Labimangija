package ee.ut.labimangija.arraygrader.labimanguhindaja;

import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.pistemeetod.PistemeetodiPiste;

import java.util.List;

public class PistemeetodiLäbimänguHindaja extends LäbimänguHindaja {


    @Override
    protected int leiaRaskusparameeter(List<Massiivioperatsioon> tehtudKäigud) {
        // pistemeetodi raskusparameeter on tehtavate pistete arv

        int raskusparameeter = 0;
        for (Massiivioperatsioon käik : tehtudKäigud) {
            if(käik instanceof PistemeetodiPiste) {
                raskusparameeter += 1;
            }
        }

        return raskusparameeter;
    }
}

