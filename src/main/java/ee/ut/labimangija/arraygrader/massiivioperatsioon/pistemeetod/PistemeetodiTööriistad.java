package ee.ut.labimangija.arraygrader.massiivioperatsioon.pistemeetod;

import static ee.ut.labimangija.arraygrader.MassiiviTööriistad.kopeeriJaSorteeriMassiiv;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class PistemeetodiTööriistad {
    public static boolean valedElemendidEnneIndeksit(int[] massiiv, int indeks) {
        // kontrollib, kas massiivis on enne indeksit elemendid, mis sorteeritud massiivi korral seal ei oleks
        int[] sorteeritudMassiiv = kopeeriJaSorteeriMassiiv(massiiv);
        for (int i = 0; i < indeks; i++) {
            if (sorteeritudMassiiv[i] != massiiv[i]) return true;
        }
        return false;
    }
}

