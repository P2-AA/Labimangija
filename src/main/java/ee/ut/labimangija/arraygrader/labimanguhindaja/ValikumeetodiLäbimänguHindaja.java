package ee.ut.labimangija.arraygrader.labimanguhindaja;

import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiElementideVahetamine;

import java.util.List;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

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

