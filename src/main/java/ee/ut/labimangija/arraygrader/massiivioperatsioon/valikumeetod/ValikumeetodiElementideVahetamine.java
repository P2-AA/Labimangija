package ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod;

import ee.ut.labimangija.arraygrader.MassiiviTööriistad;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.ElementideVahetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class ValikumeetodiElementideVahetamine extends ElementideVahetamine {
    public ValikumeetodiElementideVahetamine(int üheVahetatavaIndeks, int teiseVahetatavaIndeks, MassiiviSeis massiivEnneOperatsiooni) {
        super(üheVahetatavaIndeks, teiseVahetatavaIndeks, massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        if (getSeis().kasTööalaValimata()) { // see on võimalik ainult vea korral
            return new ValikumeetodiTööalaValimine(0, getSeis().getMassiiv().length, getSeis());
        }
        if (getSeis().getTööalaleJärgnevIndeks() - getSeis().getTööalaAlgusIndeks() == 1) { // see on võimalik ainult vea korral
            return new LäbimänguLõpetamine(getSeis());
        }
        return new ValikumeetodiTööalaValimine(getSeis().getTööalaAlgusIndeks() + 1, getSeis().getTööalaleJärgnevIndeks(), getSeis());
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        if (getSeis().kasTööalaValimata()) {
            return true;
        }
        if (!MassiiviTööriistad.kasTööalaÜmbrusOnSorteeritud(getSeis())) {
            return false;
        }
        return MassiiviTööriistad.tööalaAlgusestVähimaElemendiIndeks(getSeis()) == getSeis().getTööalaAlgusIndeks();
    }
}

