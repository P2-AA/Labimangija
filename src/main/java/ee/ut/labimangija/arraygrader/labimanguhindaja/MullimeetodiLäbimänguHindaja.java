package ee.ut.labimangija.arraygrader.labimanguhindaja;

import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod.MullimeetodiElementideVahetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod.MullimeetodiTööalaValimine;

import java.util.List;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MullimeetodiLäbimänguHindaja extends LäbimänguHindaja {
    @Override
    protected int leiaRaskusparameeter(List<Massiivioperatsioon> tehtudKäigud) {
        int raskusparameeter = 0;

        for (int i = 0; i < tehtudKäigud.size(); i++) {
            if (tehtudKäigud.get(i) instanceof MullimeetodiTööalaValimine
                    && järgnebVahetus(tehtudKäigud, i + 1)) {
                raskusparameeter++;
            }
        }
        return raskusparameeter;
    }

    private boolean järgnebVahetus(List<Massiivioperatsioon> tehtudKäigud, int algusIndeks) {
        for (int i = algusIndeks; i < tehtudKäigud.size(); i++) {
            Massiivioperatsioon käik = tehtudKäigud.get(i);
            if (käik instanceof MullimeetodiElementideVahetamine) {
                return true;
            }
            if (käik instanceof MullimeetodiTööalaValimine) {
                return false;
            }
        }
        return false;
    }
}

