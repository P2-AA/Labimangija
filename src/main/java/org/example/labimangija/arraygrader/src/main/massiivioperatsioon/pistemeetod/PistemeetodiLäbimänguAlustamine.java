package org.example.labimangija.arraygrader.massiivioperatsioon.pistemeetod;


import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

public class PistemeetodiLäbimänguAlustamine extends LäbimänguAlustamine {
    public PistemeetodiLäbimänguAlustamine(MassiiviSeis massiivEnneOperatsiooni) {
        super(massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        return new PistemeetodiTööalaValimine(0, 1, getSeis());
    }

    @Override
    public String toString() {
        return "Pistemeetodi läbimängu alustamine massiivil " + getSeis() + ".";
    }

}

