package ee.ut.labimangija.arraygrader.massiivioperatsioon.mullimeetod;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MullimeetodiTööriistad {
    protected static MullimeetodiElementideVahetamine leiaJärgmineVahetus(MassiiviSeis massiiviSeis, int algusIndeks, int lõpuIndeks) {
        int[] massiiv = massiiviSeis.getMassiiv();
        int vasakPiir = Math.max(algusIndeks, massiiviSeis.getTööalaAlgusIndeks());
        int paremPiir = Math.min(lõpuIndeks, massiiviSeis.getTööalaleJärgnevIndeks() - 1);

        for (int paremIndeks = paremPiir; paremIndeks > vasakPiir; paremIndeks--) {
            if (massiiv[paremIndeks - 1] > massiiv[paremIndeks]) {
                return new MullimeetodiElementideVahetamine(paremIndeks - 1, paremIndeks, massiiviSeis);
            }
        }
        return null;
    }
}

