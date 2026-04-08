package org.example.labimangija.arraygrader.labimanguhindaja;

import org.example.labimangija.arraygrader.MassiiviTööriistad;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

import java.util.ArrayList;
import java.util.List;

public abstract class LäbimänguHindaja {

    public Hindamistulemus hinda(List<Massiivioperatsioon> tehtudKäigud) {
        if (!(tehtudKäigud.get(0) instanceof LäbimänguAlustamine)) {
            throw new IllegalArgumentException("Esimene käik peab olema läbimängu alustamine.");
        }
        if (!(tehtudKäigud.get(tehtudKäigud.size() - 1) instanceof LäbimänguLõpetamine)) {
            throw new IllegalArgumentException("Viimane käik peab olema läbimängu lõpetamine.");
        }

        Hindamistulemus hindamistulemus = new Hindamistulemus();

        Massiivioperatsioon viimaneKäik = tehtudKäigud.get(0);

        for (int i = 1; i < tehtudKäigud.size(); i++) {
            Massiivioperatsioon praeguneKäik = tehtudKäigud.get(i);

            if (praeguneKäik.equals(viimaneKäik.järgmineÕigeKäik())) {
                hindamistulemus.suurendaÕigeteKäikudeArvu();
            } else {
                hindamistulemus.suurendaValedeKäikudeArvu();

                if (!praeguneKäik.läbimänguOnVõimalikJätkata()
                        && !(praeguneKäik instanceof LäbimänguLõpetamine
                        && MassiiviTööriistad.kasÕigeTulemus(praeguneKäik.getSeis()))) {
                    hindamistulemus.setOlulineViga(i);
                    break;
                }
            }
            viimaneKäik = praeguneKäik;
        }

        hindamistulemus.setRaskusparameeter(this.leiaRaskusparameeter(hindamistulemus.getOluliseVeaIndeks() == null
                ? tehtudKäigud : tehtudKäigud.subList(0, hindamistulemus.getOluliseVeaIndeks())));
        hindamistulemus.setOodatudRaskusparameeter(this.leiaOodatudRaskusparameeter(tehtudKäigud.get(0)));
        return hindamistulemus;

    }

    protected abstract int leiaRaskusparameeter(List<Massiivioperatsioon> tehtudKäigud);

    protected int leiaOodatudRaskusparameeter(Massiivioperatsioon esimeneKäik) {
        List<Massiivioperatsioon> käigud = new ArrayList<>();

        Massiivioperatsioon viimatineKäik = esimeneKäik;
        käigud.add(viimatineKäik);

        while (!(viimatineKäik instanceof LäbimänguLõpetamine)) {
            viimatineKäik = viimatineKäik.järgmineÕigeKäik();
            käigud.add(viimatineKäik);
        }

        return leiaRaskusparameeter(käigud);
    }
}
