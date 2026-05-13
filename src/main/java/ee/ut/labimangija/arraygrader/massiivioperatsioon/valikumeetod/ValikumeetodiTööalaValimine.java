package ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod;

import ee.ut.labimangija.arraygrader.MassiiviTööriistad;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.TööalaValimine;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class ValikumeetodiTööalaValimine extends TööalaValimine {
    public ValikumeetodiTööalaValimine(int uusTööalaAlgus, int uusTööalaleJärgnevIndeks, MassiiviSeis massiivEnneOperatsiooni) {
        super(uusTööalaAlgus, uusTööalaleJärgnevIndeks, massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        if (getSeis().getTööalaleJärgnevIndeks() - getSeis().getTööalaAlgusIndeks() == 1) {
            return new LäbimänguLõpetamine(getSeis());
        }
        int vähimaElemendiIndeks = MassiiviTööriistad.tööalaAlgusestVähimaElemendiIndeks(getSeis());
        if (vähimaElemendiIndeks == getSeis().getTööalaAlgusIndeks()) {
            return new ValikumeetodiTööalaValimine(getSeis().getTööalaAlgusIndeks() + 1, getSeis().getTööalaleJärgnevIndeks(), getSeis());
        }
        return new ValikumeetodiElementideVahetamine(getSeis().getTööalaAlgusIndeks(), vähimaElemendiIndeks, getSeis());
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        return MassiiviTööriistad.kasTööalaÜmbrusOnSorteeritud(getSeis());
    }
}

