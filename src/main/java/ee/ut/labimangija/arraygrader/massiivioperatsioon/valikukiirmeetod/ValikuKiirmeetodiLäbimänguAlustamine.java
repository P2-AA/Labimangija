package ee.ut.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class ValikuKiirmeetodiLäbimänguAlustamine extends LäbimänguAlustamine {
    ValikuKiirmeetodiMassiiviSeis valikuKiirmeetodiMassiiviSeis;

    public ValikuKiirmeetodiLäbimänguAlustamine(ValikuKiirmeetodiMassiiviSeis massiivEnneOperatsiooni) {
        super(massiivEnneOperatsiooni);
    }

    @Override
    public ValikuKiirmeetodiMassiiviSeis getSeis() {
        return valikuKiirmeetodiMassiiviSeis;
    }

    @Override
    public void setSeis(MassiiviSeis seis) {
        if (seis instanceof ValikuKiirmeetodiMassiiviSeis uusSeis) {
            this.valikuKiirmeetodiMassiiviSeis = uusSeis;
        } else {
            throw new RuntimeException("Valiku kiirmeetodi seis peab olema ValikuKiirmeetodiMassiiviSeis isend.");
        }
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        return new ValikuKiirmeetodiTööalaValimine(0, getSeis().getMassiiv().length, getSeis());
    }

    @Override
    public String toString() {
        return "Valiku kiirmeetodi läbimängu alustamine massiivil " + getSeis() + ".";
    }
}

