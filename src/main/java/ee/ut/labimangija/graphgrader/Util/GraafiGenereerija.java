package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.Popups;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// Laiuti läbimise, sügavuti läbimise, Prim'i ja eeldusgraafi sisendite genereerimise 
// loogika on loodud tehisaru abiga, et programm töötaks kuna kasutusel olevas
// Uku Hannes Arismaa töös puudusid meetodid.

// Selle klassi esialgse struktuuri koostamisel kasutati ChatGPT mudeli 5.5 abi
// Tehisaru kasutati graafi sisendite genereerimise loogika kavandamiseks ja näidisstruktuuri
// kavandamiseks.

// Iga Uku Hannes Arismaa programmist võetud loogikale on koodi sees kommentaariga viidatud.
// Tehisaru abiga loodud meetoditele on samuti viidatud kommentaaridega.

// Töö autor kontrollis, kohandas ja testis lahenduste vastavust programmi nõuetele,
// lõplikud funktsioonid, parameetrid ja integreerimine loodud läbimänguprogrammiga on autori tehtud.

public class GraafiGenereerija {

    public enum Tyyp {
        SIDUS_KAALUTUD,
        EELDUS,
        LABIMINE,
        KAHN,
        DIJKSTRA,
        BELLMAN_FORD,
        FLOYD_WARSHALL
    }

    private record Parameetrid(int n, int m, int min, int max) {
    }

    public static String genereeriFail(Tyyp tyyp) {
        Parameetrid vaike = vaikeParameetrid(tyyp);
        Parameetrid p = kysiParameetrid(tyyp, vaike);
        if (p == null) return null;

        try {
            List<String> sisu = looSisu(tyyp, p);
            if (sisu == null) return null;
            Path kaustTee = GraafiValija.sisendiKaust(tyyp);
            Files.createDirectories(kaustTee);
            Path fail = kaustTee.resolve(AppPaths.generatedFile());
            Files.write(fail, sisu);
            return fail.toAbsolutePath().toString();
        } catch (IOException e) {
            Popups.showError("Genereerimine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    private static Parameetrid vaikeParameetrid(Tyyp tyyp) {
        return switch (tyyp) {
            case LABIMINE -> new Parameetrid(10, 20, 1, 1);
            case KAHN -> new Parameetrid(10, 22, 1, 1);
            case SIDUS_KAALUTUD -> new Parameetrid(10, 15, 1, 15);
            case EELDUS -> new Parameetrid(10, 20, 1, 9);
            case DIJKSTRA -> new Parameetrid(10, 25, 0, 0);
            case BELLMAN_FORD -> new Parameetrid(10, 25, 5, 5);
            case FLOYD_WARSHALL -> new Parameetrid(8, 20, 10, 10);
        };
    }

    // Meetodi koostamisel kasutati ChatGPT mudeli 5.5 abi.
    // Tehisaru pakkus esialgse lahendusidee ja loogika, mida autor kohandas,
    // vastavalt rakenduse nõuetele.
    private static Parameetrid kysiParameetrid(Tyyp tyyp, Parameetrid vaike) {
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        Label nLbl = new Label("Tippude arv (n)");
        TextField nFld = new TextField(String.valueOf(vaike.n));
        Label mLbl = new Label("Kaarte arv (m)");
        TextField mFld = new TextField(String.valueOf(vaike.m));

        String minTxt = switch (tyyp) {
            case EELDUS -> "Min tipu aeg";
            case DIJKSTRA, BELLMAN_FORD, FLOYD_WARSHALL -> "Parameeter";
            default -> "Min kaal";
        };
        String maxTxt = switch (tyyp) {
            case EELDUS -> "Max tipu aeg";
            case DIJKSTRA -> "Paranduste arv";
            case BELLMAN_FORD -> "Kauguste puu sügavus";
            case FLOYD_WARSHALL -> "Paranduste arv";
            default -> "Max kaal";
        };

        Label minLbl = new Label(minTxt);
        TextField minFld = new TextField(String.valueOf(vaike.min));
        Label maxLbl = new Label(maxTxt);
        TextField maxFld = new TextField(String.valueOf(vaike.max));

        grid.add(nLbl, 0, 0);
        grid.add(nFld, 1, 0);
        grid.add(mLbl, 0, 1);
        grid.add(mFld, 1, 1);
        if (kasLisaparameetrid(tyyp)) {
            if (tyyp == Tyyp.DIJKSTRA || tyyp == Tyyp.BELLMAN_FORD || tyyp == Tyyp.FLOYD_WARSHALL) {
                grid.add(maxLbl, 0, 2);
                grid.add(maxFld, 1, 2);
            } else {
                grid.add(minLbl, 0, 2);
                grid.add(minFld, 1, 2);
                grid.add(maxLbl, 0, 3);
                grid.add(maxFld, 1, 3);
            }
        }

        GridPane.setHgrow(nFld, Priority.ALWAYS);
        GridPane.setHgrow(mFld, Priority.ALWAYS);
        GridPane.setHgrow(minFld, Priority.ALWAYS);
        GridPane.setHgrow(maxFld, Priority.ALWAYS);
        pane.setContent(grid);

        Alert a = new Alert(Alert.AlertType.NONE);
        a.setTitle("Genereerimise parameetrid");
        a.setHeaderText("Määra genereerimise parameetrid");
        a.setDialogPane(pane);

        Optional<ButtonType> vastus = a.showAndWait();
        if (vastus.isEmpty() || vastus.get() != ButtonType.OK) return null;

        try {
            int n = Integer.parseInt(nFld.getText().trim());
            int m = Integer.parseInt(mFld.getText().trim());
            int min = vaike.min;
            int max = vaike.max;
            if (kasLisaparameetrid(tyyp)) {
                if (tyyp == Tyyp.DIJKSTRA || tyyp == Tyyp.BELLMAN_FORD || tyyp == Tyyp.FLOYD_WARSHALL) {
                    max = Integer.parseInt(maxFld.getText().trim());
                } else {
                    min = Integer.parseInt(minFld.getText().trim());
                    max = Integer.parseInt(maxFld.getText().trim());
                }
            }
            return new Parameetrid(n, m, min, max);
        } catch (NumberFormatException e) {
            Popups.showError("Parameetrid peavad olema täisarvud.");
            return null;
        }
    }

    private static boolean kasLisaparameetrid(Tyyp tyyp) {
        return switch (tyyp) {
            case SIDUS_KAALUTUD, EELDUS, DIJKSTRA, BELLMAN_FORD, FLOYD_WARSHALL -> true;
            default -> false;
        };
    }

    private static List<String> looSisu(Tyyp tyyp, Parameetrid p) {
        if (p.n < 2) {
            Popups.showError("Tippude arv peab olema >= 2");
            return null;
        }
        if ((tyyp == Tyyp.SIDUS_KAALUTUD || tyyp == Tyyp.EELDUS) && p.min > p.max) {
            Popups.showError("Miinimum ei tohi olla suurem kui maksimum.");
            return null;
        }

        return switch (tyyp) {
            case SIDUS_KAALUTUD -> genereeriSidusKaalutud(p.n, p.m, p.min, p.max);
            case EELDUS -> genereeriEeldus(p.n, p.m, p.min, p.max);
            case LABIMINE -> genereeriLabimiseGraaf(p.n, p.m);
            case KAHN -> vormindaKaalutaGraaf(p.n, genereeriKahnProgramm(p.n, p.m));
            case DIJKSTRA -> vormindaKaalugaGraaf(p.n, genereeriDijkstraProgramm(p.n, p.m, p.max));
            case BELLMAN_FORD -> vormindaKaalugaGraaf(p.n, genereeriBellmanFordProgramm(p.n, p.m, p.max));
            case FLOYD_WARSHALL -> vormindaMaatriksGraafiks(genereeriFwProgramm(p.n, p.m, p.max));
        };
    }

    // Meetodi koostamisel kasutati ChatGPT mudeli 5.5 abi.
    // Tehisaru pakkus esialgse lahendusidee ja loogika, mida autor kohandas,
    // vastavalt rakenduse nõuetele.
    private static List<String> genereeriLabimiseGraaf(int n, int m) {
        int minM = n - 1;
        int maxM = n * (n - 1);
        if (m < minM || m > maxM) {
            Popups.showError("Kaarte arv peab olema vahemikus " + minM + ".." + maxM);
            return null;
        }

        Random r = new Random();
        boolean[][] olemas = new boolean[n][n];
        List<int[]> kaared = new ArrayList<>();

        List<Integer> jarjekord = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            jarjekord.add(i);
        }
        Collections.shuffle(jarjekord, r);

        List<Integer> saavutatud = new ArrayList<>();
        saavutatud.add(0);
        for (int tipp : jarjekord) {
            int vanem = saavutatud.get(r.nextInt(saavutatud.size()));
            lisaKaar(kaared, olemas, vanem, tipp);
            saavutatud.add(tipp);
        }

        List<int[]> kandidaadid = koikSuunatudKaared(n);
        eemaldaOlemasolevad(kandidaadid, olemas, false);
        Collections.shuffle(kandidaadid, r);
        while (kaared.size() < m) lisaKaar(kaared, olemas, kandidaadid.remove(kandidaadid.size() - 1));
        return vormindaKaalutaGraaf(n, kaared);
    }

    // Loogika Uku Hannes Arismaa programmist, meetodist Kahn(int n, int m).
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7
    private static List<int[]> genereeriKahnProgramm(int n, int m) {
        if (m > n * (n - 1) / 2 || m < n - 1) {
            Popups.showError("Kaarte arv pole sobiv.");
            return null;
        }

        HashMapIntList ep = new HashMapIntList(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) if (i < j) ep.get(i).add(j);
        }
        ArrayList<int[]> e = new ArrayList<>();

        Random r = new Random();
        int labitudP = r.nextInt(n);
        int paremale = labitudP + 1;
        int vasakule = labitudP - 1;
        for (int k = 0; k < n - 1; k++) {
            boolean p = true;
            if (r.nextBoolean()) {
                p = paremale != n;
            } else if (vasakule != -1) {
                p = false;
            }
            int i = r.nextInt(paremale - vasakule - 1) + vasakule + 1;
            int[] kaar;
            if (p) {
                kaar = new int[]{i, paremale};
                paremale++;
            } else {
                kaar = new int[]{vasakule, i};
                vasakule--;
            }
            ep.get(kaar[0]).remove(Integer.valueOf(kaar[1]));
            e.add(kaar);
        }

        ArrayList<int[]> ep2 = new ArrayList<>();
        for (int i = 0; i < n; i++) for (int j : ep.get(i)) ep2.add(new int[]{i, j});
        for (int i = n - 1; i < m; i++) {
            int ind = r.nextInt(ep2.size());
            e.add(ep2.get(ind));
            ep2.set(ind, ep2.get(ep2.size() - 1));
            ep2.remove(ep2.size() - 1);
        }
        return e;
    }

    // Loogika Uku Hannes Arismaa programmist, meetodist Dijkstra(int n, int m, int p).
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7
    private static List<int[]> genereeriDijkstraProgramm(int n, int m, int p) {
        if (m > n * (n - 1) || m < n - 1) {
            Popups.showError("Kaarte arv pole sobiv.");
            return null;
        }
        if (p > n * (n - 1) / 2 - n + 1 || p > m - n + 1 || p < 0) {
            Popups.showError("Paranduste arv pole sobiv.");
            return null;
        }

        ArrayList<int[]> e = new ArrayList<>();
        ArrayList<ArrayList<Integer>> ep = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ep.add(new ArrayList<>());
            for (int j = 0; j < n; j++) if (i < j) ep.get(i).add(j);
        }
        Random r = new Random();

        for (int i = 1; i < n; i++) {
            int j = r.nextInt(i);
            e.add(new int[]{j, i, 0});
            ep.get(j).remove(Integer.valueOf(i));
        }

        ArrayList<int[]> ep2 = new ArrayList<>();
        for (int i = 0; i < ep.size(); i++) for (int j : ep.get(i)) ep2.add(new int[]{i, j, 0});

        for (int i = 0; i < p; i++) {
            int ind = r.nextInt(ep2.size());
            e.add(ep2.get(ind));
            ep2.set(ind, ep2.get(ep2.size() - 1));
            ep2.remove(ep2.size() - 1);
        }

        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) if (i > j) ep2.add(new int[]{i, j, 0});

        ArrayList<int[]> ev = new ArrayList<>();
        ArrayList<int[]> es = new ArrayList<>();
        for (int i = 0; i < m - p - n + 1; i++) {
            int ind = r.nextInt(ep2.size());
            int[] kaar = ep2.get(ind);
            if (kaar[0] < kaar[1]) ev.add(kaar);
            else es.add(kaar);
            ep2.set(ind, ep2.get(ep2.size() - 1));
            ep2.remove(ep2.size() - 1);
        }

        int[][] vahimad = new int[n][];
        int[][] vahimadEv = new int[n][];
        int[][] suurimad = new int[n][];
        for (int i = 0; i < n; i++) {
            vahimad[i] = new int[]{n, -1};
            vahimadEv[i] = new int[]{n, -1};
            suurimad[i] = new int[]{-1, -1};
        }

        for (int i = 0; i < e.size(); i++) {
            int[] kaar = e.get(i);
            if (kaar[0] < vahimad[kaar[1]][0]) vahimad[kaar[1]] = new int[]{kaar[0], i};
            if (kaar[0] > suurimad[kaar[1]][0]) suurimad[kaar[1]] = new int[]{kaar[0], i};
        }
        for (int i = 0; i < ev.size(); i++) {
            int[] kaar = ev.get(i);
            if (kaar[0] < vahimadEv[kaar[1]][0]) vahimadEv[kaar[1]] = new int[]{kaar[0], i};
        }
        for (int i = 0; i < n; i++) {
            if (vahimadEv[i][0] < vahimad[i][0]) {
                e.get(vahimad[i][1])[0] = vahimadEv[i][0];
                ev.get(vahimadEv[i][1])[0] = vahimad[i][0];
            }
        }

        int[] kaugus = new int[n];
        for (int i = 0; i < n; i++) kaugus[i] = 1_000_000;
        kaugus[0] = 0;

        for (int i = 1; i < n; i++) {
            int[] kaar = e.get(suurimad[i][1]);
            kaugus[i] = kaugus[i - 1] + r.nextInt(20) + 1;
            kaar[2] = kaugus[kaar[1]] - kaugus[kaar[0]];
        }

        for (int[] kaar : es) kaar[2] = r.nextInt(20) + 1;

        for (int i = n - 1; i > -1; i--) {
            for (int[] kaar : e) {
                if (kaar[0] == i && kaar[2] == 0) {
                    kaugus[kaar[1]] += r.nextInt(20) + 1;
                    kaar[2] = kaugus[kaar[1]] - kaugus[kaar[0]];
                }
            }
            for (int[] kaar : ev) {
                if (kaar[0] == i) kaar[2] = kaugus[kaar[1]] - kaugus[kaar[0]] + r.nextInt(20) + 21;
            }
        }

        e.addAll(ev);
        e.addAll(es);
        return e;
    }

    // Loogika võetud Uku Hannes Arismaa programmist, meetodist BFntsüklita(int n, int m, int l).
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7
    private static List<int[]> genereeriBellmanFordProgramm(int n, int m, int l) {

        if (m > n * (n - 1) || m < n - 1) {
            Popups.showError("Kaarte arv pole sobiv.");
            return null;
        }
        if (l < 1 || l > n - 1) {
            Popups.showError("Kauguste puu sügavus pole sobiv.");
            return null;
        }

        ArrayList<int[]> e = new ArrayList<>();
        ArrayList<ArrayList<Integer>> ep = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ep.add(new ArrayList<>());
            for (int j = 0; j < n; j++) if (i != j) ep.get(i).add(j);
        }

        int[] sygavus = new int[n];
        for (int i = 0; i < n; i++) sygavus[i] = 1_000_000;
        sygavus[0] = 0;

        for (int i = 0; i < l; i++) {
            ep.get(i).remove(Integer.valueOf(i + 1));
            sygavus[i + 1] = sygavus[i] + 1;
            e.add(new int[]{i, i + 1, 0});
        }

        Random r = new Random();
        ArrayList<int[]> kohad = new ArrayList<>();
        for (int i = 0; i < l; i++) kohad.add(new int[]{i, i});

        for (int i = l + 1; i < n; i++) {
            int j = r.nextInt(kohad.size());
            if (kohad.get(j)[1] != l - 1) kohad.add(new int[]{i, kohad.get(j)[1] + 1});
            e.add(new int[]{j, i, 0});
            ep.get(j).remove(Integer.valueOf(i));
        }

        int[] kaugus = new int[n];
        for (int i = 0; i < n; i++) kaugus[i] = 1_000_000;
        kaugus[0] = 0;

        for (int[] kaar : e) {
            kaar[2] = r.nextInt(10) + 1;
            kaugus[kaar[1]] = kaugus[kaar[0]] + kaar[2];
        }

        ArrayList<int[]> ep2 = new ArrayList<>();
        for (int i = 0; i < ep.size(); i++) for (int j : ep.get(i)) ep2.add(new int[]{i, j, 0});
        for (int i = 0; i < m - n + 1; i++) {
            int ind = r.nextInt(ep2.size());
            int[] kaar = ep2.get(ind);
            ep2.set(ind, ep2.get(ep2.size() - 1));
            ep2.remove(ep2.size() - 1);
            kaar[2] = kaugus[kaar[1]] - kaugus[kaar[0]] + r.nextInt(20) + 1;
            e.add(kaar);
        }
        return e;
    }

    // Loogika võetud Uku Hannes Arismaa programmist, meetodist FW(int n, int m, int p).
    // Sama algoritmiline kuju esineb ka failis FW.py funktsioonis FW(n, m, p).
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7
    private static int[][] genereeriFwProgramm(int n, int m, int p) {
        if (m > n * (n - 1)) {
            Popups.showError("Kaarte arv pole sobiv.");
            return null;
        }

        int p2 = 0;
        int[][] e = new int[n][n];
        int m2;
        Random r = new Random();
        while (p2 != p) {
            m2 = m;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) e[i][j] = 1_000_000;
                e[i][i] = 0;
            }

            HashMapIntList ep = new HashMapIntList(n + 1);
            for (int i = -1; i < n; i++) {
                ep.ensure(i);
                for (int j = -1; j < n; j++) if (i != j) ep.get(i).add(j);
            }

            int[] kaugus = new int[n + 1];
            for (int i = 0; i < n + 1; i++) kaugus[i] = 1_000_000;
            kaugus[0] = 0;

            for (int i = 0; i < n; i++) {
                if (m2 <= 0) break;
                int j = r.nextInt(i + 1) - 1;
                ep.get(j).remove(Integer.valueOf(i));
                int kaal = r.nextInt(10) + 1;
                kaugus[i + 1] = kaugus[j + 1] + kaal;
                if (j != -1) {
                    e[j][i] = kaal;
                    m2--;
                }
            }

            ArrayList<int[]> ep2 = new ArrayList<>();
            for (int i : ep.keys()) for (int j : ep.get(i)) ep2.add(new int[]{i, j, 0});

            while (m2 > 0) {
                int ind = r.nextInt(ep2.size());
                int[] kaar = ep2.get(ind);
                ep2.set(ind, ep2.get(ep2.size() - 1));
                ep2.remove(ep2.size() - 1);
                int kaal = kaugus[kaar[1] + 1] - kaugus[kaar[0] + 1] + r.nextInt(20) + 1;
                if (kaar[0] != -1 && kaar[1] != -1) {
                    e[kaar[0]][kaar[1]] = kaal;
                    m2--;
                }
            }

            int[][] e2 = new int[n][n];
            for (int i = 0; i < n; i++) System.arraycopy(e[i], 0, e2[i], 0, n);

            p2 = 0;
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (e[i][k] + e[k][j] < e[i][j]) {
                            if (e[i][j] == 1_000_000) p2--;
                            e[i][j] = e[i][k] + e[k][j];
                            p2++;
                        }
                    }
                }
            }
            for (int i = 0; i < n; i++) System.arraycopy(e2[i], 0, e[i], 0, n);
        }
        return e;
    }

    // Meetodi koostamisel kasutati ChatGPT mudeli 5.5 abi.
    // Tehisaru pakkus esialgse lahendusidee ja loogika, mida autor kohandas,
    // vastavalt rakenduse nõuetele.
    private static List<String> genereeriSidusKaalutud(int n, int m, int minKaal, int maxKaal) {
        int minM = n - 1;
        int maxM = n * (n - 1) / 2;
        if (m < minM || m > maxM) {
            Popups.showError("Kaarte arv peab olema vahemikus " + minM + ".." + maxM);
            return null;
        }

        Random r = new Random();
        boolean[][] olemas = new boolean[n][n];
        List<int[]> kaared = new ArrayList<>();

        List<Integer> jarjekord = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            jarjekord.add(i);
        }
        Collections.shuffle(jarjekord, r);

        List<Integer> puus = new ArrayList<>();
        puus.add(0);
        for (int tipp : jarjekord) {
            int naaber = puus.get(r.nextInt(puus.size()));
            lisaServ(kaared, olemas, tipp, naaber, r.nextInt(maxKaal - minKaal + 1) + minKaal);
            puus.add(tipp);
        }

        List<int[]> kandidaadid = koikServad(n);
        eemaldaOlemasolevad(kandidaadid, olemas, true);
        Collections.shuffle(kandidaadid, r);
        while (kaared.size() < m) {
            int[] serv = kandidaadid.remove(kandidaadid.size() - 1);
            lisaServ(kaared, olemas, serv[0], serv[1], r.nextInt(maxKaal - minKaal + 1) + minKaal);
        }
        return vormindaKaalugaGraaf(n, kaared);
    }

    // Meetodi koostamisel kasutati ChatGPT mudeli 5.5 abi.
    // Tehisaru pakkus esialgse lahendusidee ja loogika, mida autor kohandas,
    // vastavalt rakenduse nõuetele.
    private static List<String> genereeriEeldus(int n, int m, int minAeg, int maxAeg) {
        int minM = n - 1;
        int maxM = n * (n - 1) / 2;
        if (m < minM || m > maxM) {
            Popups.showError("Kaarte arv peab olema vahemikus " + minM + ".." + maxM);
            return null;
        }

        List<int[]> kaared = genereeriKahnProgramm(n, m);
        if (kaared == null) return null;

        Random r = new Random();
        int[] teisendus = looTeisendus(n, r);
        StringBuilder algus = new StringBuilder("p edge ").append(n).append(" ").append(m);
        for (int i = 0; i < n; i++) algus.append(" ").append(r.nextInt(minAeg, maxAeg + 1));

        List<String> read = new ArrayList<>();
        read.add(algus.toString());
        for (int[] kaar : kaared) read.add("e " + teisendus[kaar[0]] + " " + teisendus[kaar[1]]);
        return read;
    }


    // Teisendusloogika võetud Uku Hannes Arismaa programmist, meetoditest prindig(...).
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7
    private static List<String> vormindaKaalutaGraaf(int n, List<int[]> kaared) {
        if (kaared == null) return null;
        int[] teisendus = looTeisendus(n, new Random());
        List<String> read = new ArrayList<>();
        read.add("p edge " + n + " " + kaared.size());
        for (int[] kaar : kaared) read.add("e " + teisendus[kaar[0]] + " " + teisendus[kaar[1]]);
        return read;
    }

    // Teisendusloogika võetud Uku Hannes Arismaa programmist, meetoditest prindigp(...).
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7
    private static List<String> vormindaKaalugaGraaf(int n, List<int[]> kaared) {
        if (kaared == null) return null;
        int[] teisendus = looTeisendus(n, new Random());
        List<String> read = new ArrayList<>();
        read.add("p edge " + n + " " + kaared.size());
        for (int[] kaar : kaared) read.add("e " + teisendus[kaar[0]] + " " + teisendus[kaar[1]] + " " + kaar[2]);
        return read;
    }

    // Meetodi koostamisel kasutati ChatGPT mudeli 5.5 abi.
    // Tehisaru pakkus esialgse lahendusidee ja loogika, mida autor kohandas,
    // vastavalt rakenduse nõuetele.
    private static List<String> vormindaMaatriksGraafiks(int[][] maatriks) {
        if (maatriks == null) return null;
        int n = maatriks.length;
        List<int[]> kaared = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && maatriks[i][j] != 1_000_000) kaared.add(new int[]{i, j, maatriks[i][j]});
            }
        }
        return vormindaKaalugaGraaf(n, kaared);
    }

    // Otsene vaste Uku Hannes Arismaa programmi meetodite prindig(...) / prindigp(...) teisenduste osale.
    // https://thesis.cs.ut.ee/eefc18a7-fc02-4ec1-9c63-1765db239ef7 
    private static int[] looTeisendus(int n, Random r) {
        ArrayList<Integer> teisendus = new ArrayList<>();
        for (int i = 1; i < n; i++) teisendus.add(i + 1);
        Collections.shuffle(teisendus, r);
        teisendus.add(0, 1);
        int[] tulemus = new int[n];
        for (int i = 0; i < n; i++) tulemus[i] = teisendus.get(i);
        return tulemus;
    }

    private static List<int[]> koikSuunatudKaared(int n) {
        List<int[]> kaared = new ArrayList<>();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) if (i != j) kaared.add(new int[]{i, j});
        return kaared;
    }

    private static List<int[]> koikDagKaared(int n) {
        List<int[]> kaared = new ArrayList<>();
        for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) kaared.add(new int[]{i, j});
        return kaared;
    }

    private static List<int[]> koikServad(int n) {
        List<int[]> servad = new ArrayList<>();
        for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) servad.add(new int[]{i, j});
        return servad;
    }

    private static void eemaldaOlemasolevad(List<int[]> kandidaadid, boolean[][] olemas, boolean suunamata) {
        kandidaadid.removeIf(kaar -> olemas[kaar[0]][kaar[1]] || (suunamata && olemas[kaar[1]][kaar[0]]));
    }

    private static void lisaKaar(List<int[]> kaared, boolean[][] olemas, int algus, int lopp) {
        olemas[algus][lopp] = true;
        kaared.add(new int[]{algus, lopp});
    }

    private static void lisaKaar(List<int[]> kaared, boolean[][] olemas, int algus, int lopp, int kaal) {
        olemas[algus][lopp] = true;
        kaared.add(new int[]{algus, lopp, kaal});
    }

    private static void lisaKaar(List<int[]> kaared, boolean[][] olemas, int[] kaar) {
        if (kaar.length >= 3) lisaKaar(kaared, olemas, kaar[0], kaar[1], kaar[2]);
        else lisaKaar(kaared, olemas, kaar[0], kaar[1]);
    }

    private static void lisaServ(List<int[]> kaared, boolean[][] olemas, int a, int b, int kaal) {
        int algus = Math.min(a, b);
        int lopp = Math.max(a, b);
        olemas[algus][lopp] = true;
        kaared.add(new int[]{algus, lopp, kaal});
    }

    // Meetodi koostamisel kasutati ChatGPT mudeli 5.5 abi.
    // Tehisaru pakkus esialgse lahendusidee ja loogika, mida autor kohandas,
    // vastavalt rakenduse nõuetele.
    private static final class HashMapIntList {
        private final ArrayList<Integer> votmed = new ArrayList<>();
        private final ArrayList<ArrayList<Integer>> vaartused = new ArrayList<>();

        HashMapIntList(int ignored) {
        }

        void ensure(int voti) {
            if (!votmed.contains(voti)) {
                votmed.add(voti);
                vaartused.add(new ArrayList<>());
            }
        }

        ArrayList<Integer> get(int voti) {
            ensure(voti);
            return vaartused.get(votmed.indexOf(voti));
        }

        List<Integer> keys() {
            return new ArrayList<>(votmed);
        }
    }
}
