package org.example.labimangija.arraygrader.labimanguhindaja;

import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.mullimeetod.MullimeetodiPiste;

import java.util.List;

public class MullimeetodiLäbimänguHindaja extends LäbimänguHindaja {
    @Override
    protected int leiaRaskusparameeter(List<Massiivioperatsioon> tehtudKäigud) {
        // mullimeetodi raskusparameeter on massiivi läbimiste arv
        // loen kokku, mitu korda piste alguskoht on eelmise piste lõpust paremal

        int raskusparameeter = 0;
        int viimasePisteLõpp = Integer.MIN_VALUE;

        for (Massiivioperatsioon käik : tehtudKäigud) {
            if (käik instanceof MullimeetodiPiste piste) {
                // kui praeguse piste alguskoht on eelmise piste lõpust paremal, siis eeldame, et alustati uut ringi
                if (piste.getPisteAlgusIndeks() > viimasePisteLõpp) {
                    raskusparameeter++;
                }
                viimasePisteLõpp = piste.getPisteLõpuIndeks();
            }
        }
        return raskusparameeter;
    }
}
