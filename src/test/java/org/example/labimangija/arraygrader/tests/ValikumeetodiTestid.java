package org.example.labimangija.arraygrader.tests;

import org.example.labimangija.arraygrader.labimanguhindaja.ValikumeetodiLäbimänguHindaja;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiElementideVahetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiLäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiTööalaValimine;
import org.example.labimangija.arraygrader.tests.tooriistad.IndeksiteGenereerimine;

import java.util.ArrayList;
import java.util.List;

public class ValikumeetodiTestid extends Testid {
    ValikumeetodiTestid() {
        this.läbimänguHindaja = new ValikumeetodiLäbimänguHindaja();
    }

    @Override
    LäbimänguAlustamine uueLäbimänguAlustamiseOperatsioon() {
        return new ValikumeetodiLäbimänguAlustamine(looUusMassiiviSeis());
    }

    @Override
    List<Massiivioperatsioon> kõikvõimalikudKäigud(MassiiviSeis massiiviSeis) {
        List<Massiivioperatsioon> võimalikudKäigud = new ArrayList<>();

        // Läbimängu lõpetamine
        võimalikudKäigud.add(new LäbimänguLõpetamine(massiiviSeis));

        // Elementide vahetamine
        List<IndeksiteGenereerimine.VahetatavadIndeksid> elementideVahetuseIndeksid = IndeksiteGenereerimine.leiaKõikVõimalikudVahetusteIndeksid(massiiviSeis.getMassiiv().length);
        for (IndeksiteGenereerimine.VahetatavadIndeksid indeksitePaar : elementideVahetuseIndeksid) {
            võimalikudKäigud.add(new ValikumeetodiElementideVahetamine(indeksitePaar.vahetatav1(), indeksitePaar.vahetatav2(), massiiviSeis));
        }

        // Tööala valimine
        List<IndeksiteGenereerimine.TööalaIndeksid> tööalaMuutmiseIndeksid = IndeksiteGenereerimine.leiaKõikvõimalikudTööalaValimiseIndeksid(massiiviSeis.getMassiiv().length);
        for (IndeksiteGenereerimine.TööalaIndeksid indeksitePaar : tööalaMuutmiseIndeksid) {
            võimalikudKäigud.add(new ValikumeetodiTööalaValimine(indeksitePaar.algus(), indeksitePaar.lõpustJärgmine(), massiiviSeis));
        }

        return võimalikudKäigud;
    }
}
