package ee.ut.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.TööalaValimine;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class ValikuKiirmeetodiTööalaValimine extends TööalaValimine {
    ValikuKiirmeetodiMassiiviSeis valikuKiirmeetodiMassiiviSeis;

    public ValikuKiirmeetodiTööalaValimine(int uusTööalaAlgus, int uusTööalaleJärgnevIndeks, ValikuKiirmeetodiMassiiviSeis massiivEnneOperatsiooni) {
        super(uusTööalaAlgus, uusTööalaleJärgnevIndeks, massiivEnneOperatsiooni);
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
        if (getSeis().getVastusePiir() < getSeis().getTööalaAlgusIndeks()
                || getSeis().getVastusePiir() >= getSeis().getTööalaleJärgnevIndeks()) { // kui vastuse piir on tööalast väljas; see on võimalik ainult vea korral
            return new LäbimänguLõpetamine(getSeis());
        }
        return ValikuKiirmeetodiTööriistad.leiaLahkmeJärgiJaotamine(getSeis());
    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        if (getSeis().getVastusePiir() < getSeis().getTööalaAlgusIndeks()
                || getSeis().getVastusePiir() >= getSeis().getTööalaleJärgnevIndeks()) {
            return ValikuKiirmeetodiTööriistad.kasVähimadElemendidOnEes(valikuKiirmeetodiMassiiviSeis);
        }
        return ValikuKiirmeetodiTööriistad.kasEnneTööalaOnAinultVähimadElemendid(getSeis())
                && ValikuKiirmeetodiTööriistad.kasKõikPiiristVäiksemadOnTööalasVõiEnneSeda(getSeis());
    }
}

