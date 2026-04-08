package org.example.labimangija.arraygrader.tests;

import org.example.labimangija.arraygrader.labimanguhindaja.ValikuKiirmeetodiLäbimänguHindaja;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiLahkmeJärgiJaotamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiLäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiTööalaValimine;
import org.example.labimangija.arraygrader.tests.tooriistad.IndeksiteGenereerimine;
import org.example.labimangija.arraygrader.tests.tooriistad.VõimalikudMassiiviJärjestused;

import java.util.ArrayList;
import java.util.List;

public class ValikuKiirmeetodiTestid extends Testid {

    int otsitavateElementideArv = 3;

    ValikuKiirmeetodiTestid() {
        this.läbimänguHindaja = new ValikuKiirmeetodiLäbimänguHindaja();
    }

    @Override
    LäbimänguAlustamine uueLäbimänguAlustamiseOperatsioon() {
        ValikuKiirmeetodiMassiiviSeis massiiviSeis = new ValikuKiirmeetodiMassiiviSeis(looUusMassiiviSeis().getMassiiv(), null, null, otsitavateElementideArv);
        return new ValikuKiirmeetodiLäbimänguAlustamine(massiiviSeis);
    }

    @Override
    List<Massiivioperatsioon> kõikvõimalikudKäigud(MassiiviSeis massiiviSeis) {
        if (!(massiiviSeis instanceof ValikuKiirmeetodiMassiiviSeis valikuKiirmeetodiMassiiviSeis)) {
            throw new RuntimeException("Antud massiiviseis peab olema valiku kiirmeetodi oma");
        }

        List<Massiivioperatsioon> võimalikudKäigud = new ArrayList<>();

        // Läbimängu lõpetamine
        võimalikudKäigud.add(new LäbimänguLõpetamine(massiiviSeis));

        // Lahkme järgi jaotamine
        int[][] massiiviJärjestused = new VõimalikudMassiiviJärjestused().leiaVõimalikudJärjestused(valikuKiirmeetodiMassiiviSeis.getMassiiv());
        for (int[] järjestus : massiiviJärjestused) {
            ValikuKiirmeetodiMassiiviSeis uusMassiiviSeis = new ValikuKiirmeetodiMassiiviSeis(järjestus, massiiviSeis.getTööalaAlgusIndeks(), massiiviSeis.getTööalaleJärgnevIndeks(), ((ValikuKiirmeetodiMassiiviSeis) massiiviSeis).getVastusePiir());
            for (int i = 0; i <= massiiviSeis.getMassiiv().length; i++) {
                võimalikudKäigud.add(new ValikuKiirmeetodiLahkmeJärgiJaotamine(uusMassiiviSeis, i));
            }
        }

        // Tööala valimine
        List<IndeksiteGenereerimine.TööalaIndeksid> tööalaMuutmiseIndeksid = IndeksiteGenereerimine.leiaKõikvõimalikudTööalaValimiseIndeksid(massiiviSeis.getMassiiv().length);
        for (IndeksiteGenereerimine.TööalaIndeksid indeksitePaar : tööalaMuutmiseIndeksid) {
            võimalikudKäigud.add(new ValikuKiirmeetodiTööalaValimine(indeksitePaar.algus(), indeksitePaar.lõpustJärgmine(), valikuKiirmeetodiMassiiviSeis));
        }

        return võimalikudKäigud;
    }

}
