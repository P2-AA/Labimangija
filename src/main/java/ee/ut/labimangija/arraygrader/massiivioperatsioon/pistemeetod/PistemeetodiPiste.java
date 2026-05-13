package ee.ut.labimangija.arraygrader.massiivioperatsioon.pistemeetod;


import ee.ut.labimangija.arraygrader.MassiiviTööriistad;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Piste;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class PistemeetodiPiste extends Piste {
    public PistemeetodiPiste(int pisteAlgusIndeks, int pisteLõpuIndeks, MassiiviSeis massiivEnnePistet) {
        super(pisteAlgusIndeks, pisteLõpuIndeks, massiivEnnePistet);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        if (getSeis().kasTööalaValimata()) { // see on võimalik ainult vea korral
            return new PistemeetodiTööalaValimine(0, 1, getSeis());
        }
        if (getSeis().getMassiiv().length == getSeis().getTööalaleJärgnevIndeks()) {
            return new LäbimänguLõpetamine(getSeis());
        }
        return new PistemeetodiTööalaValimine(getSeis().getTööalaAlgusIndeks(), getSeis().getTööalaleJärgnevIndeks() + 1, getSeis());
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        if (getSeis().kasTööalaValimata()) {
            return true;
        }
        if (PistemeetodiTööriistad.valedElemendidEnneIndeksit(getSeis().getMassiiv(), getSeis().getTööalaAlgusIndeks())) {
            return false;
        }
        return MassiiviTööriistad.kasVahemikOnSorteeritud(getSeis().getMassiiv(), getSeis().getTööalaAlgusIndeks(), getSeis().getTööalaleJärgnevIndeks());
    }
}


