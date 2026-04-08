package org.example.labimangija.arraygrader.massiivioperatsioon.mullimeetod;

import org.example.labimangija.arraygrader.MassiiviTööriistad;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.TööalaValimine;

public class MullimeetodiTööalaValimine extends TööalaValimine {
    public MullimeetodiTööalaValimine(int uusTööalaAlgus, int uusTööalaleJärgnevIndeks, MassiiviSeis massiivEnneOperatsiooni) {
        super(uusTööalaAlgus, uusTööalaleJärgnevIndeks, massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        if (!MassiiviTööriistad.kasVahemikOnSorteeritud(getSeis().getMassiiv(), getSeis().getTööalaAlgusIndeks(), getSeis().getTööalaleJärgnevIndeks())) {
            return MullimeetodiTööriistad.leiaJärgminePiste(getSeis(), getSeis().getTööalaAlgusIndeks(), getSeis().getTööalaleJärgnevIndeks() - 1);
        }
        return new LäbimänguLõpetamine(getSeis());
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        return MassiiviTööriistad.kasTööalaÜmbrusOnSorteeritud(getSeis());
    }
}
