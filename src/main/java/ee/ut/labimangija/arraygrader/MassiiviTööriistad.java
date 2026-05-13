package ee.ut.labimangija.arraygrader;


import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiTööriistad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MassiiviTööriistad {
    public static boolean kasTööalaÜmbrusOnSorteeritud(MassiiviSeis massiiviSeis) {
        // kas enne ja pärast tööala on täpselt need elemendid, nagu sorteeritud massiivis oleks
        int[] sorteeritudMassiiv = kopeeriJaSorteeriMassiiv(massiiviSeis.getMassiiv());
        for (int i = 0; i < massiiviSeis.getTööalaAlgusIndeks(); i++) {
            if (massiiviSeis.getMassiiv()[i] != sorteeritudMassiiv[i]) return false;
        }
        for (int i = massiiviSeis.getTööalaleJärgnevIndeks(); i < massiiviSeis.getMassiiv().length; i++) {
            if (massiiviSeis.getMassiiv()[i] != sorteeritudMassiiv[i]) return false;
        }
        return true;
    }

    public static int tööalaAlgusestVähimaElemendiIndeks(MassiiviSeis massiiviSeis) {
        int vähimaIndeks = massiiviSeis.getTööalaAlgusIndeks();
        int vähimaVäärtus = massiiviSeis.getMassiiv()[vähimaIndeks];
        for (Integer i = massiiviSeis.getTööalaAlgusIndeks(); i < massiiviSeis.getTööalaleJärgnevIndeks(); i++) {
            if (massiiviSeis.getMassiiv()[i] < vähimaVäärtus) {
                vähimaIndeks = i;
                vähimaVäärtus = massiiviSeis.getMassiiv()[vähimaIndeks];
            }
        }
        return vähimaIndeks;
    }

    public static int[] kopeeriJaSorteeriMassiiv(int[] massiiv) {
        int[] koopiaMassiiv = Arrays.copyOf(massiiv, massiiv.length);
        Arrays.sort(koopiaMassiiv);
        return koopiaMassiiv;
    }

    public static boolean kasÕigeTulemus(MassiiviSeis massiiviSeis) {
        // valiku kiirmeetodi puhul hindab kas on vähimad elemendid ees. muul juhul kas on sorteeritud.
        if (massiiviSeis instanceof ValikuKiirmeetodiMassiiviSeis valikuKiirmeetodiMassiiviSeis) {
            return ValikuKiirmeetodiTööriistad.kasVähimadElemendidOnEes(valikuKiirmeetodiMassiiviSeis);
        }

        return MassiiviTööriistad.kasVahemikOnSorteeritud(massiiviSeis.getMassiiv(), 0, massiiviSeis.getMassiiv().length);
    }

    public static boolean kasVahemikOnSorteeritud(int[] massiiv, int algusIndeks, int lõpuIndeks) {
        // kontrollib, kas antud vahemik eraldiseisvalt on sorteeritud
        // lõpuindeks välja arvatud
        for (int i = algusIndeks + 1; i < lõpuIndeks; i++) {
            if (massiiv[i] < massiiv[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public static List<Massiivioperatsioon> kopeeriKäigudJajätkaLäbimängu(List<Massiivioperatsioon> praeguseniTehtudkäigud, Massiivioperatsioon järgmineKäik) {
        // teeb listist koopia, lisab argumendiks antud järgmise käigu ja proovib jätkata läbimängu õigesti
        // (kuigi edasine läbimäng ei pruugi õige olla, kui lisatud käik oli vale)
        List<Massiivioperatsioon> tehtudKäigud = new ArrayList<>(praeguseniTehtudkäigud);
        tehtudKäigud.add(järgmineKäik);

        Massiivioperatsioon praeguneKäik = järgmineKäik;
        while (!(praeguneKäik instanceof LäbimänguLõpetamine)) {
            praeguneKäik = praeguneKäik.järgmineÕigeKäik();
            tehtudKäigud.add(praeguneKäik);
        }
        return tehtudKäigud;
    }
}


