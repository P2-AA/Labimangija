package org.example.labimangija.arraygrader.kasutajaliides;

import org.example.labimangija.arraygrader.labimanguhindaja.LäbimänguHindaja;
import org.example.labimangija.arraygrader.labimanguhindaja.ValikuKiirmeetodiLäbimänguHindaja;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiLahkmeJärgiJaotamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiLäbimänguAlustamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.valikukiirmeetod.ValikuKiirmeetodiTööalaValimine;

import java.util.Arrays;

public class ValikuKiirmeetodiKasutajaliides extends Kasutajaliides {
    @Override
    protected void kuvaVõimalikudOperatsioonid() {
        System.out.println("jaota a b _ c d e - muudab massiivi seisu, alakriips märgib jaotamise lahkmekohta");
        super.kuvaVõimalikudOperatsioonid();
    }


    @Override
    protected LäbimänguAlustamine läbimänguAlustamiseOperatsioon(int[] massiiv) {
        return new ValikuKiirmeetodiLäbimänguAlustamine(new ValikuKiirmeetodiMassiiviSeis(massiiv, null, null, valikuKiirmeetodiVastusePiir));
    }

    @Override
    protected void läbimänguAlustamiseSõnum(int[] massiiv) {
        System.out.println("Alustame valiku kiirmeetodi läbimängu massiivil " + Arrays.toString(massiiv) + ", tuua esimesed " + valikuKiirmeetodiVastusePiir + " elementi massiivi algusesse.");
    }

    @Override
    protected Massiivioperatsioon leiaOperatsioon(String[] sisend, MassiiviSeis massiiviSeis) throws ViganeSisendException {
        if (!(massiiviSeis instanceof ValikuKiirmeetodiMassiiviSeis valikuKiirmeetodiMassiiviSeis)) {
            throw new IllegalArgumentException("Valiku kiirmeetodi massiivi seis peab olema ValikuKiirmeetodiMassiiviSeis-tüüpi.");
        }
        switch (sisend[0]) {
            case "jaota":
                if (sisend.length != valikuKiirmeetodiMassiiviSeis.getMassiiv().length + 2) {
                    throw new ViganeSisendException("Uue massiivi pikkus peab olema sama, kui vana massiivi pikkus. Lisaks peab olema märgitud lahkmekoht.");
                }
                int[] uusMassiiv = new int[valikuKiirmeetodiMassiiviSeis.getMassiiv().length];
                int lahkmeIndeks = -1;
                int parandus = 0;
                for (int i = 1; i < sisend.length; i++) {
                    if (sisend[i].equals("_")) {
                        parandus = -1;
                        lahkmeIndeks = i - 1;
                    } else {
                        try {
                            uusMassiiv[i - 1 + parandus] = Integer.parseInt(sisend[i]);
                        } catch (NumberFormatException e) {
                            throw new ViganeSisendException("Jaotamise käigul tohib kasutada ainult sümbolit _ ja numbreid.");
                        }
                    }
                }
                if (lahkmeIndeks == -1) {
                    throw new ViganeSisendException("Lahe peab olema märgitud.");
                }
                return new ValikuKiirmeetodiLahkmeJärgiJaotamine(new ValikuKiirmeetodiMassiiviSeis(uusMassiiv,
                        valikuKiirmeetodiMassiiviSeis.getTööalaAlgusIndeks(),
                        valikuKiirmeetodiMassiiviSeis.getTööalaleJärgnevIndeks(),
                        valikuKiirmeetodiMassiiviSeis.getVastusePiir()), lahkmeIndeks);
            case "tööala":
                if(sisend.length != 3) {
                    throw new ViganeSisendException("Tööala muutmine vajab kahte argumenti (tööala algus ja lõpp).");
                }
                int tööalaAlgus = Integer.parseInt(sisend[1]);
                int tööalaLõpp = Integer.parseInt(sisend[2]);
                return new ValikuKiirmeetodiTööalaValimine(tööalaAlgus, tööalaLõpp, valikuKiirmeetodiMassiiviSeis);
            default:
                throw new ViganeSisendException("Vigane käsk.");
        }
    }

    @Override
    protected LäbimänguHindaja läbimänguHindaja() {
        return new ValikuKiirmeetodiLäbimänguHindaja();
    }
}
