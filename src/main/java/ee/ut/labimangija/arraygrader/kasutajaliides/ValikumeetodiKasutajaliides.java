package ee.ut.labimangija.arraygrader.kasutajaliides;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiElementideVahetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiLäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.valikumeetod.ValikumeetodiTööalaValimine;

import java.util.Arrays;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class ValikumeetodiKasutajaliides extends Kasutajaliides {
    @Override
    protected LäbimänguAlustamine läbimänguAlustamiseOperatsioon(int[] massiiv) {
        return new ValikumeetodiLäbimänguAlustamine(new MassiiviSeis(massiiv, null, null));
    }

    @Override
    protected Massiivioperatsioon leiaOperatsioon(String[] sisend, MassiiviSeis massiiviSeis) throws ViganeSisendException {
        switch (sisend[0]) {
            case "vaheta":
                if (sisend.length != 3) {
                    throw new ViganeSisendException("Elementide vahetamine vajab kahte argumenti.");
                }
                int indeks1 = Integer.parseInt(sisend[1]);
                int indeks2 = Integer.parseInt(sisend[2]);
                return new ValikumeetodiElementideVahetamine(indeks1, indeks2, massiiviSeis);
            case "tööala":
                if (sisend.length != 3) {
                    throw new ViganeSisendException("Tööala muutmine vajab kahte argumenti (tööala algus ja lõpp).");
                }
                int tööalaAlgus = Integer.parseInt(sisend[1]);
                int tööalaLõpp = Integer.parseInt(sisend[2]);
                return new ValikumeetodiTööalaValimine(tööalaAlgus, tööalaLõpp, massiiviSeis);
            default:
                throw new ViganeSisendException("Vigane käsk.");
        }
    }

}

