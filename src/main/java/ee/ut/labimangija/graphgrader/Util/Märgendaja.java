package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.graphgrader.Graaf.Graaf;
import ee.ut.labimangija.graphgrader.Graaf.Tipp;

import java.util.LinkedList;
import java.util.Queue;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class Märgendaja {

    public static void margenda(Graaf g) {
        Queue<Tipp> jrk = new LinkedList<>();
        g.tipud.get(0).syg = 0;
        jrk.add(g.tipud.get(0));

        int maxS = 0;
        while (!jrk.isEmpty()) {
            Tipp praegune = jrk.poll();
            for (Tipp tipp : praegune.alluvad) {
                if (tipp.syg == -1) {
                    tipp.syg = praegune.syg + 1;
                    maxS = Math.max(maxS, tipp.syg);
                }
                jrk.add(tipp);
            }
        }


    }
}


