package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.graphgrader.Graaf.Graaf;
import ee.ut.labimangija.graphgrader.Graaf.Tipp;

import java.util.LinkedList;
import java.util.Queue;


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


