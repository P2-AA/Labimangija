package ee.ut.labimangija.arraygrader.labimanguhindaja;

import ee.ut.labimangija.arraygrader.MassiiviTööriistad;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

import java.util.ArrayList;
import java.util.List;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

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

