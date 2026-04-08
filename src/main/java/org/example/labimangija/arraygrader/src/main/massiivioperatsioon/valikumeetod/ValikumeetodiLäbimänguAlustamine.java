package org.example.labimangija.arraygrader.massiivioperatsioon.valikumeetod;

import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

public class ValikumeetodiLäbimänguAlustamine extends LäbimänguAlustamine {
    public ValikumeetodiLäbimänguAlustamine(MassiiviSeis massiivEnneOperatsiooni) {
        super(massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        return new ValikumeetodiTööalaValimine(0, getSeis().getMassiiv().length, getSeis());
    }

    @Override
    public String toString() {
        return "Valikumeetodi läbimängu alustamine massiivil " + getSeis() + ".";
    }
}
