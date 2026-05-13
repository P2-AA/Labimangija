package ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod;

import ee.ut.labimangija.arraygrader.MassiiviTööriistad;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.ElementideVahetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MullimeetodiElementideVahetamine extends ElementideVahetamine {
    public MullimeetodiElementideVahetamine(int üheVahetatavaIndeks, int teiseVahetatavaIndeks, MassiiviSeis massiivEnneOperatsiooni) {
        super(üheVahetatavaIndeks, teiseVahetatavaIndeks, massiivEnneOperatsiooni);
        if (Math.abs(üheVahetatavaIndeks - teiseVahetatavaIndeks) != 1) {
            throw new IllegalArgumentException("Mullimeetodis tohib vahetada ainult kõrvutiasetsevaid elemente.");
        }
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        if (getSeis().kasTööalaValimata()) {
            return new MullimeetodiTööalaValimine(0, getSeis().getMassiiv().length, getSeis());
        }

        MullimeetodiElementideVahetamine järgmineVahetus = MullimeetodiTööriistad.leiaJärgmineVahetus(
                getSeis(),
                getSeis().getTööalaAlgusIndeks(),
                vasakpoolseElemendiIndeks
        );
        if (järgmineVahetus != null) {
            return järgmineVahetus;
        }

        int uusAlgus = getSeis().getTööalaAlgusIndeks() + 1;
        if (getSeis().getTööalaleJärgnevIndeks() - uusAlgus <= 1) {
            return new LäbimänguLõpetamine(getSeis());
        }
        return new MullimeetodiTööalaValimine(uusAlgus, getSeis().getTööalaleJärgnevIndeks(), getSeis());
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        return !getSeis().kasTööalaValimata()
                && MassiiviTööriistad.kasTööalaÜmbrusOnSorteeritud(getSeis());
    }
}
