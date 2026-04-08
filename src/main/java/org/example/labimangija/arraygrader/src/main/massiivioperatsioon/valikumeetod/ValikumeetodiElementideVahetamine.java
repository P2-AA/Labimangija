package org.example.labimangija.arraygrader.massiivioperatsioon.valikumeetod;

import org.example.labimangija.arraygrader.MassiiviTööriistad;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.ElementideVahetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

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
