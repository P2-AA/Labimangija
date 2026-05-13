package ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MullimeetodiLäbimänguAlustamine extends LäbimänguAlustamine {
    public MullimeetodiLäbimänguAlustamine(MassiiviSeis massiivEnneOperatsiooni) {
        super(massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        return new MullimeetodiTööalaValimine(0, getSeis().getMassiiv().length, getSeis());
    }

    @Override
    public String toString() {
        return "Mullimeetodi läbimängu alustamine massiivil " + getSeis() + ".";
    }
}

