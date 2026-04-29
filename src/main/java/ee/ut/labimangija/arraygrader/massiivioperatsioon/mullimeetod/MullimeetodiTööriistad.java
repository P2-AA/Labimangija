package ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;

public class MullimeetodiTööriistad {
    protected static MullimeetodiPiste leiaJärgminePiste(MassiiviSeis massiiviSeis, int vasakpoolneOts, int parempoolneOts) {
        // leiab alates paremalt esimese sobiva elemendi ja pistab ta endast vasakule esimesse võimalikku kohta
        // parempoolneOts on paremalt esimese elemendi indeks
        for (int pisteAlgus = parempoolneOts; pisteAlgus > vasakpoolneOts; pisteAlgus--) {
            int uuePisteLõpp = pisteAlgus;
            while (uuePisteLõpp > vasakpoolneOts
                    && massiiviSeis.getMassiiv()[pisteAlgus] < massiiviSeis.getMassiiv()[uuePisteLõpp - 1]) {
                uuePisteLõpp--;
            }
            if (pisteAlgus != uuePisteLõpp) {
                return new MullimeetodiPiste(pisteAlgus, uuePisteLõpp, massiiviSeis);
            }
        }
        throw new RuntimeException("Pistet ei ole võimalik teha.");
    }


}

