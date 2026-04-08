package org.example.labimangija.arraygrader.tests;

import org.example.labimangija.arraygrader.labimanguhindaja.PistemeetodiLäbimänguHindaja;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.pistemeetod.PistemeetodiLäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.pistemeetod.PistemeetodiPiste;
import org.example.labimangija.arraygrader.massiivioperatsioon.pistemeetod.PistemeetodiTööalaValimine;
import org.example.labimangija.arraygrader.tests.tooriistad.IndeksiteGenereerimine;

import java.util.ArrayList;
import java.util.List;

public class PistemeetodiTestid extends Testid {
    PistemeetodiTestid() {
        this.läbimänguHindaja = new PistemeetodiLäbimänguHindaja();
    }

    @Override
    LäbimänguAlustamine uueLäbimänguAlustamiseOperatsioon() {
        return new PistemeetodiLäbimänguAlustamine(looUusMassiiviSeis());
    }

    @Override
    List<Massiivioperatsioon> kõikvõimalikudKäigud(MassiiviSeis massiiviSeis) {
        List<Massiivioperatsioon> võimalikudKäigud = new ArrayList<>();

        // Läbimängu lõpetamine
        võimalikudKäigud.add(new LäbimänguLõpetamine(massiiviSeis));

        // Piste tegemine
        List<IndeksiteGenereerimine.PisteIndeksid> pisteteIndeksid = IndeksiteGenereerimine.leiaKõikVõimalikudPisteIndeksid(massiiviSeis.getMassiiv().length);
        for (IndeksiteGenereerimine.PisteIndeksid indeksitePaar : pisteteIndeksid) {
            võimalikudKäigud.add(new PistemeetodiPiste(indeksitePaar.algus(), indeksitePaar.lõpp(), massiiviSeis));
        }

        // Tööala valimine
        List<IndeksiteGenereerimine.TööalaIndeksid> tööalaMuutmiseIndeksid = IndeksiteGenereerimine.leiaKõikvõimalikudTööalaValimiseIndeksid(massiiviSeis.getMassiiv().length);
        for (IndeksiteGenereerimine.TööalaIndeksid indeksitePaar : tööalaMuutmiseIndeksid) {
            võimalikudKäigud.add(new PistemeetodiTööalaValimine(indeksitePaar.algus(), indeksitePaar.lõpustJärgmine(), massiiviSeis));
        }

        return võimalikudKäigud;
    }

}
