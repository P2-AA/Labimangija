package ee.ut.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LahkmeJärgiJaotamine;

import java.util.Arrays;

public class ValikuKiirmeetodiTööriistad {

    protected static LahkmeJärgiJaotamine leiaLahkmeJärgiJaotamine(ValikuKiirmeetodiMassiiviSeis massiiviSeis) {
        int lahe = massiiviSeis.getMassiiv()[massiiviSeis.getTööalaAlgusIndeks()];

        int[] uusMassiiv = Arrays.copyOf(massiiviSeis.getMassiiv(), massiiviSeis.getMassiiv().length);

        int i = massiiviSeis.getTööalaAlgusIndeks();
        int j = massiiviSeis.getTööalaleJärgnevIndeks() - 1;
        int esimeneLahkmestSuurem;

        while (true) {
            while (uusMassiiv[j] >= lahe) {
                j--;
                if (j < massiiviSeis.getTööalaAlgusIndeks()) { // kõik tööala elemendid on lahkmest suuremad või võrdsed
                    return new ValikuKiirmeetodiLahkmeJärgiJaotamine(massiiviSeis, massiiviSeis.getTööalaAlgusIndeks());
                }
            }
            while (uusMassiiv[i] < lahe) {
                i++;
            }
            if (i > j) { // kui positsioonid läksid üksteisest mööda
                esimeneLahkmestSuurem = i;
                MassiiviSeis uusMassiiviSeis = new ValikuKiirmeetodiMassiiviSeis(uusMassiiv, massiiviSeis.getTööalaAlgusIndeks(),
                        massiiviSeis.getTööalaleJärgnevIndeks(), massiiviSeis.getVastusePiir());
                return new ValikuKiirmeetodiLahkmeJärgiJaotamine(uusMassiiviSeis, esimeneLahkmestSuurem);
            }
            int abi = uusMassiiv[i];
            uusMassiiv[i] = uusMassiiv[j];
            uusMassiiv[j] = abi;
        }
    }

    public static boolean kasVähimadElemendidOnEes(ValikuKiirmeetodiMassiiviSeis massiiviSeis) {
        // kontroll kas valiku kiirmeetodi lõpptulemus on õige

        // panen läbimängu tulemusel saadud vähimad n elementi massiivi ja sorteerin ära
        int[] leitudVähimadElemendid = new int[massiiviSeis.getVastusePiir()];
        for (int i = 0; i < leitudVähimadElemendid.length; i++) {
            leitudVähimadElemendid[i] = massiiviSeis.getMassiiv()[i];
        }
        Arrays.sort(leitudVähimadElemendid);

        // sorteerin algse massiivi ära
        int[] sorteeritudMassiiv = Arrays.copyOf(massiiviSeis.getMassiiv(), massiiviSeis.getMassiiv().length);
        Arrays.sort(sorteeritudMassiiv);

        // kui esimesed n elementi ei ole mõlemas massiivis samad, ei ole vähimad elemendid ees
        for (int i = 0; i < leitudVähimadElemendid.length; i++) {
            if (leitudVähimadElemendid[i] != sorteeritudMassiiv[i]) {
                return false;
            }
        }

        return true;
    }

    protected static boolean kasEnneTööalaOnAinultVähimadElemendid(ValikuKiirmeetodiMassiiviSeis massiiviSeis) {
        // kontrollib, kas enne tööala asuvad ainult need elemendid, mis jäävad vastusesse peale korrektset valiku kiirmeetodi läbimängu
        int[] elemendidEnneTööala = leiaEsimesedElemendidSorteeritult(massiiviSeis, massiiviSeis.getTööalaAlgusIndeks());

        int[] algneMassiiv = Arrays.copyOf(massiiviSeis.getMassiiv(), massiiviSeis.getMassiiv().length);
        Arrays.sort(algneMassiiv);

        int j = 0;
        for (int element : elemendidEnneTööala) {
            while (algneMassiiv[j] != element && j < massiiviSeis.getVastusePiir()) {
                j++;
            }
            if (j >= massiiviSeis.getVastusePiir()) {
                return false;
            }
            j++;
        }
        return true;
    }

    protected static int[] leiaEsimesedElemendidSorteeritult(ValikuKiirmeetodiMassiiviSeis massiiviSeis, int mitmendaElemendini) {
        // leiab elemendid, mis oleksid valiku kiirmeetodi järel esimeses osas ja sorteerib need
        int[] esimesed = new int[mitmendaElemendini];
        for (int i = 0; i < esimesed.length; i++) {
            esimesed[i] = massiiviSeis.getMassiiv()[i];
        }
        Arrays.sort(esimesed);
        return esimesed;
    }

    protected static boolean kasKõikPiiristVäiksemadOnTööalasVõiEnneSeda(ValikuKiirmeetodiMassiiviSeis massiiviSeis) {
        // kontrollib, ega mõni n vähima elemendi hulgast pole peale tööala
        if (massiiviSeis.getTööalaleJärgnevIndeks() < massiiviSeis.getVastusePiir()) {
            return false;
        }
        int[] tööalaJaEelnevad = leiaEsimesedElemendidSorteeritult(massiiviSeis, massiiviSeis.getTööalaleJärgnevIndeks());

        int[] algneMassiiv = Arrays.copyOf(massiiviSeis.getMassiiv(), massiiviSeis.getMassiiv().length);
        Arrays.sort(algneMassiiv);

        for (int i = 0; i < massiiviSeis.getVastusePiir(); i++) {
            if (tööalaJaEelnevad[i] != algneMassiiv[i]) {
                return false;
            }
        }
        return true;
    }

}

