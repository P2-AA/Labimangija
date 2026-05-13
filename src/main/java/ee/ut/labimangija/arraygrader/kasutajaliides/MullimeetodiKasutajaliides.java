package ee.ut.labimangija.arraygrader.kasutajaliides;

import ee.ut.labimangija.arraygrader.labimanguhindaja.LäbimänguHindaja;
import ee.ut.labimangija.arraygrader.labimanguhindaja.MullimeetodiLäbimänguHindaja;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod.MullimeetodiElementideVahetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod.MullimeetodiLäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod.MullimeetodiTööalaValimine;

import java.util.Arrays;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MullimeetodiKasutajaliides extends Kasutajaliides {
    @Override
    protected void kuvaVõimalikudOperatsioonid() {
        System.out.println("vaheta <indeks1> <indeks2> - vahetab kõrvutiasetsevad elemendid");
        super.kuvaVõimalikudOperatsioonid();
    }

    @Override
    protected LäbimänguAlustamine läbimänguAlustamiseOperatsioon(int[] massiiv) {
        return new MullimeetodiLäbimänguAlustamine(new MassiiviSeis(massiiv, null, null));
    }

    @Override
    protected void läbimänguAlustamiseSõnum(int[] massiiv) {
        System.out.println("Alustame mullimeetodi läbimängu massiivil " + Arrays.toString(massiiv)
                + ". Kasutame mullimeetodi versiooni, kus vahetatakse ainult kõrvutiasetsevaid elemente");
    }

    @Override
    protected Massiivioperatsioon leiaOperatsioon(String[] sisend, MassiiviSeis massiiviSeis) throws ViganeSisendException {
        switch (sisend[0]) {
            case "vaheta":
                if(sisend.length != 3) {
                    throw new ViganeSisendException("Vahetus vajab kahte argumenti (esimene ja teine indeks).");
                }
                int esimeneIndeks = Integer.parseInt(sisend[1]);
                int teineIndeks = Integer.parseInt(sisend[2]);
                return new MullimeetodiElementideVahetamine(esimeneIndeks, teineIndeks, massiiviSeis);
            case "tööala":
                if(sisend.length != 3) {
                    throw new ViganeSisendException("Tööala muutmine vajab kahte argumenti (tööala algus ja lõpp).");
                }
                int tööalaAlgus = Integer.parseInt(sisend[1]);
                int tööalaLõpp = Integer.parseInt(sisend[2]);
                return new MullimeetodiTööalaValimine(tööalaAlgus, tööalaLõpp, massiiviSeis);
            default:
                throw new ViganeSisendException("Vigane käsk.");
        }
    }

    @Override
    protected LäbimänguHindaja läbimänguHindaja() {
        return new MullimeetodiLäbimänguHindaja();
    }
}
