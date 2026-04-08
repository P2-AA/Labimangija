package org.example.labimangija.arraygrader.massiivioperatsioon.valikumeetod;

import org.example.labimangija.arraygrader.MassiiviTööriistad;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.TööalaValimine;

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
