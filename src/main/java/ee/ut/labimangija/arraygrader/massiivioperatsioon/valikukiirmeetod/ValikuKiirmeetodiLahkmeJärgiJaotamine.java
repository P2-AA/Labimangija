package ee.ut.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LahkmeJärgiJaotamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class ValikuKiirmeetodiLahkmeJärgiJaotamine extends LahkmeJärgiJaotamine {

    ValikuKiirmeetodiMassiiviSeis valikuKiirmeetodiMassiiviSeis;

    public ValikuKiirmeetodiLahkmeJärgiJaotamine(MassiiviSeis massiivPealeOperatsiooni, int lahkmeJärgiJaotamisePiiristJärgnevIndeks) {
        super(massiivPealeOperatsiooni, lahkmeJärgiJaotamisePiiristJärgnevIndeks);
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
            throw new IllegalArgumentException("Valiku kiirmeetodi seis peab olema ValikuKiirmeetodiMassiiviSeis isend.");
        }
    }

    @Override
    public Massiivioperatsioon järgmineÕigeKäik() {
        //kui tööala on valimata, tuleks see valida
        if (getSeis().kasTööalaValimata()) { // see on võimalik ainult vea korral
            return new ValikuKiirmeetodiTööalaValimine(0, getSeis().getMassiiv().length, getSeis());
        }

        int praeguseJaotamisePiir = getLahkmeJärgiJaotamisePiiristJärgnevIndeks();
        int oodatavPikkus = getSeis().getVastusePiir();

        // kui jaotamise tulemuseks saadud piir eraldab nõutud hulgal elemente (või elemente ei vahetatud ja piir nihkub mõtteliselt edasi ja seejärel eraldab nõutud hulgal elemente)
        if (praeguseJaotamisePiir == oodatavPikkus
                || (praeguseJaotamisePiir + 1 == oodatavPikkus && praeguseJaotamisePiir == getSeis().getTööalaAlgusIndeks())) {
            return new LäbimänguLõpetamine(getSeis());
        }

        // uus tööala valitakse vastavalt sellele, kas jaotamise tulemuseks saadud piir eraldab nõutust rohkem või vähem elemente
        if (praeguseJaotamisePiir > oodatavPikkus) {
            return new ValikuKiirmeetodiTööalaValimine(getSeis().getTööalaAlgusIndeks(), praeguseJaotamisePiir, getSeis());
        } else {
            if (praeguseJaotamisePiir == getSeis().getTööalaAlgusIndeks()) {// kui lahkme järgi jagamine ei muutnud seisu
                return new ValikuKiirmeetodiTööalaValimine(praeguseJaotamisePiir + 1, getSeis().getTööalaleJärgnevIndeks(), getSeis());
            }
            return new ValikuKiirmeetodiTööalaValimine(praeguseJaotamisePiir, getSeis().getTööalaleJärgnevIndeks(), getSeis());
        }

    }

    @Override
    public boolean läbimänguOnVõimalikJätkata() {
        if (getSeis().kasTööalaValimata()) {
            return true;
        }
        Massiivioperatsioon järgmineÕigeKäik = järgmineÕigeKäik();

        return järgmineÕigeKäik.läbimänguOnVõimalikJätkata()
                || (järgmineÕigeKäik instanceof LäbimänguLõpetamine && ValikuKiirmeetodiTööriistad.kasVähimadElemendidOnEes(valikuKiirmeetodiMassiiviSeis));
    }
}

