package ee.ut.labimangija.arraygrader.massiivioperatsioon.pistemeetod;

import static ee.ut.labimangija.arraygrader.MassiiviTööriistad.kopeeriJaSorteeriMassiiv;

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

