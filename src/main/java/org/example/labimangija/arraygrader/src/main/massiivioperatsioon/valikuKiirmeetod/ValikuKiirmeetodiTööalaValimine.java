package org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod;

import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.TööalaValimine;

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
