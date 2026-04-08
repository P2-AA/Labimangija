package org.example.labimangija.arraygrader.massiivioperatsioon.mullimeetod;

import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

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
