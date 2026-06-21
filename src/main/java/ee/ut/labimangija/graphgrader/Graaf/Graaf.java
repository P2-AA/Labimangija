package ee.ut.labimangija.graphgrader.Graaf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import ee.ut.labimangija.common.AppPaths;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class Graaf {
    public List<Tipp> tipud;
    public boolean kaalutud;

    public Graaf(String failitee, boolean suunatud) throws IOException {
        this(failitee, suunatud, false);
    }

    public Graaf(String failitee, boolean suunatud, boolean kaalutudTipud) throws IOException {
        List<String> graaf = loeFail(failitee);

        List<Tipp> tipud = new ArrayList<>();

        String[] esimene = graaf.get(0).split(" ");

        for (int i = 1; i <= Integer.parseInt(esimene[2]); i++) {
            Tipp tipp = new Tipp((char) (i + 'A' - 1) + "");
            if (kaalutudTipud)
                tipp.kaal = Integer.parseInt(esimene[3 + i]);
            tipud.add(tipp);
        }

        boolean kaalutud = false;
        for (int i = 1; i < graaf.size(); i++) {
            String[] osad = graaf.get(i).split(" ");
            int alg = Integer.parseInt(osad[1]);
            int lopp = Integer.parseInt(osad[2]);
            Tipp algus = tipud.get(alg - 1);
            Tipp loppT = tipud.get(lopp - 1);
            algus.lisaAlluv(loppT);
            if (!suunatud) loppT.lisaAlluv(algus);
            if (osad.length == 4) {
                kaalutud = true;
                int kaal = Integer.parseInt(osad[3]);
                algus.kaared.add(new Kaar(algus, loppT, kaal));
                if (!suunatud)
                    loppT.kaared.add(new Kaar(loppT, algus, kaal));
            } else {
                algus.kaared.add(new Kaar(algus, loppT));
                if (!suunatud)
                    loppT.kaared.add(new Kaar(loppT, algus));
            }

        }

        this.tipud = tipud;
        this.kaalutud = kaalutud;
    }

    private static List<String> loeFail(String failitee) throws IOException {
        Path fail = Path.of(failitee);
        List<String> read = Files.readAllLines(fail.isAbsolute() ? fail : AppPaths.resolve(failitee));
        List<String> tagastus = new ArrayList<>();

        for (String s : read) if (!Objects.equals(s.split(" ")[0], "c")) tagastus.add(s);

        return tagastus;
    }

    public boolean onMittenegatiivne() {
        for (Tipp tipp : tipud) {
            for (Kaar kaar : tipp.kaared) {
                if (kaar.kaal < 0) return false;
            }
        }
        return true;
    }

    public boolean onAtsukliline() {
        int[] sisendastmed = new int[tipud.size()];
        for (Tipp tipp : tipud) {
            for (Tipp alluv : tipp.alluvad) {
                sisendastmed[alluv.tähis.charAt(0) - 'A']++;
            }
        }

        Queue<Tipp> nullAstmega = new LinkedList<>();
        for (Tipp tipp : tipud) {
            if (sisendastmed[tipp.tähis.charAt(0) - 'A'] == 0) {
                nullAstmega.add(tipp);
            }
        }

        int toodeldudTipud = 0;
        while (!nullAstmega.isEmpty()) {
            Tipp tipp = nullAstmega.remove();
            toodeldudTipud++;
            for (Tipp alluv : tipp.alluvad) {
                int indeks = alluv.tähis.charAt(0) - 'A';
                sisendastmed[indeks]--;
                if (sisendastmed[indeks] == 0) {
                    nullAstmega.add(alluv);
                }
            }
        }

        return toodeldudTipud == tipud.size();
    }
}
