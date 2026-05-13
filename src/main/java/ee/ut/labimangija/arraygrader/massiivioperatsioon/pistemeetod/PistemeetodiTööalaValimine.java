package ee.ut.labimangija.arraygrader.massiivioperatsioon.pistemeetod;


import ee.ut.labimangija.arraygrader.MassiiviTööriistad;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.TööalaValimine;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class PistemeetodiTööalaValimine extends TööalaValimine {

    public PistemeetodiTööalaValimine(int uusTööalaAlgus, int uusTööalaleJärgnevIndeks, MassiiviSeis massiivEnneOperatsiooni) {
        super(uusTööalaAlgus, uusTööalaleJärgnevIndeks, massiivEnneOperatsiooni);
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        MassiiviSeis seis = getSeis();
        if (seis.getTööalaleJärgnevIndeks() - seis.getTööalaAlgusIndeks() >= 2
                && seis.getMassiiv()[seis.getTööalaleJärgnevIndeks() - 1] <
                seis.getMassiiv()[seis.getTööalaleJärgnevIndeks() - 2]) {

            int elemendiIndeks = seis.getTööalaleJärgnevIndeks() - 1;
            int pistetavNumber = seis.getMassiiv()[elemendiIndeks];

            while (elemendiIndeks > seis.getTööalaAlgusIndeks() &&
                    seis.getMassiiv()[elemendiIndeks - 1] > pistetavNumber) {
                elemendiIndeks--;
            }
            return new PistemeetodiPiste(seis.getTööalaleJärgnevIndeks() - 1, elemendiIndeks, seis);
        }
        if (seis.getTööalaleJärgnevIndeks() == seis.getMassiiv().length) {
            return new LäbimänguLõpetamine(seis);
        }
        return new PistemeetodiTööalaValimine(seis.getTööalaAlgusIndeks(), seis.getTööalaleJärgnevIndeks() + 1, seis);
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        if (PistemeetodiTööriistad.valedElemendidEnneIndeksit(getSeis().getMassiiv(), getSeis().getTööalaAlgusIndeks())) {
            return false;
        }
        return MassiiviTööriistad.kasVahemikOnSorteeritud(getSeis().getMassiiv(), getSeis().getTööalaAlgusIndeks(), getSeis().getTööalaleJärgnevIndeks() - 1);
    }


}

