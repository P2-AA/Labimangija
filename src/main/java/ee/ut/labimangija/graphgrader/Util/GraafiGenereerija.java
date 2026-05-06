package ee.ut.labimangija.graphgrader.Util;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import ee.ut.labimangija.common.AppPaths;

public class GraafiGenereerija {

    public enum Tyyp {
        SUUNATUD,
        SUUNATUD_DAG,
        SUUNATUD_KAALUTUD,
        SIDUS_KAALUTUD,
        EELDUS
    }

    private record Parameetrid(int n, int m, int min, int max) {}

    public static String genereeriFail(Tyyp tyyp, String kaust) {
        Parameetrid vaike = vaikeParameetrid(tyyp);
        Parameetrid p = kysiParameetrid(tyyp, vaike);
        if (p == null) return null;

        try {
            List<String> sisu = looSisu(tyyp, p);
            if (sisu == null) return null;
            Path kaustTee = AppPaths.resolve(kaust);
            Files.createDirectories(kaustTee);
            String aeg = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path fail = kaustTee.resolve("gen_" + aeg + ".txt");
            Files.write(fail, sisu);
            return fail.toAbsolutePath().toString();
        } catch (IOException e) {
            Teavitaja.teavita("Genereerimine ebaõnnestus: " + e.getMessage(), "Viga");
            return null;
        }
    }

    private static Parameetrid vaikeParameetrid(Tyyp tyyp) {
        return switch (tyyp) {
            case SUUNATUD -> new Parameetrid(10, 20, 1, 1);
            case SUUNATUD_DAG -> new Parameetrid(10, 22, 1, 1);
            case SUUNATUD_KAALUTUD -> new Parameetrid(10, 25, 1, 15);
            case SIDUS_KAALUTUD -> new Parameetrid(10, 15, 1, 15);
            case EELDUS -> new Parameetrid(10, 20, 1, 9);
        };
    }

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

        String minTxt = tyyp == Tyyp.EELDUS ? "Min tipu aeg" : "Min kaal";
        String maxTxt = tyyp == Tyyp.EELDUS ? "Max tipu aeg" : "Max kaal";
        Label minLbl = new Label(minTxt);
        TextField minFld = new TextField(String.valueOf(vaike.min));
        Label maxLbl = new Label(maxTxt);
        TextField maxFld = new TextField(String.valueOf(vaike.max));

        grid.add(nLbl, 0, 0);
        grid.add(nFld, 1, 0);
        grid.add(mLbl, 0, 1);
        grid.add(mFld, 1, 1);
        if (tyyp == Tyyp.SUUNATUD_KAALUTUD || tyyp == Tyyp.SIDUS_KAALUTUD || tyyp == Tyyp.EELDUS) {
            grid.add(minLbl, 0, 2);
            grid.add(minFld, 1, 2);
            grid.add(maxLbl, 0, 3);
            grid.add(maxFld, 1, 3);
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
            int min = 1;
            int max = 1;
            if (tyyp == Tyyp.SUUNATUD_KAALUTUD || tyyp == Tyyp.SIDUS_KAALUTUD || tyyp == Tyyp.EELDUS) {
                min = Integer.parseInt(minFld.getText().trim());
                max = Integer.parseInt(maxFld.getText().trim());
            }
            return new Parameetrid(n, m, min, max);
        } catch (NumberFormatException e) {
            Teavitaja.teavita("Parameetrid peavad olema täisarvud.", "Viga");
            return null;
        }
    }

    private static List<String> looSisu(Tyyp tyyp, Parameetrid p) {
        if (p.n < 2) {
            Teavitaja.teavita("Tippude arv peab olema >= 2", "Viga");
            return null;
        }
        if (p.min > p.max) {
            Teavitaja.teavita("Miinimum ei tohi olla suurem kui maksimum.", "Viga");
            return null;
        }

        return switch (tyyp) {
            case SUUNATUD -> genereeriSuunatud(p.n, p.m, false, false, 1, 1);
            case SUUNATUD_DAG -> genereeriSuunatud(p.n, p.m, true, false, 1, 1);
            case SUUNATUD_KAALUTUD -> genereeriSuunatud(p.n, p.m, false, true, p.min, p.max);
            case SIDUS_KAALUTUD -> genereeriSidusKaalutud(p.n, p.m, p.min, p.max);
            case EELDUS -> genereeriEeldus(p.n, p.m, p.min, p.max);
        };
    }

    private static List<String> genereeriSuunatud(int n, int m, boolean dag, boolean kaalutud, int minKaal, int maxKaal) {
        int minM = n - 1;
        int maxM = dag ? n * (n - 1) / 2 : n * (n - 1);
        if (m < minM || m > maxM) {
            Teavitaja.teavita("Kaarte arv peab olema vahemikus " + minM + ".." + maxM, "Viga");
            return null;
        }

        Random r = new Random();
        Set<Long> olemas = new HashSet<>();
        List<int[]> e = new ArrayList<>();

        for (int i = 1; i < n; i++) {
            lisaServ(e, olemas, i, i + 1, dag);
        }
        while (e.size() < m) {
            int a = r.nextInt(n) + 1;
            int b = r.nextInt(n) + 1;
            if (a == b) continue;
            if (dag && a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            if (a == b || !lisaServ(e, olemas, a, b, dag)) continue;
        }

        List<String> read = new ArrayList<>();
        read.add("p edge " + n + " " + m);
        for (int[] kaar : e) {
            if (kaalutud) read.add("e " + kaar[0] + " " + kaar[1] + " " + (r.nextInt(maxKaal - minKaal + 1) + minKaal));
            else read.add("e " + kaar[0] + " " + kaar[1]);
        }
        return read;
    }

    private static List<String> genereeriSidusKaalutud(int n, int m, int minKaal, int maxKaal) {
        int minM = n - 1;
        int maxM = n * (n - 1) / 2;
        if (m < minM || m > maxM) {
            Teavitaja.teavita("Kaarte arv peab olema vahemikus " + minM + ".." + maxM, "Viga");
            return null;
        }

        Random r = new Random();
        Set<Long> olemas = new HashSet<>();
        List<int[]> e = new ArrayList<>();

        for (int i = 2; i <= n; i++) {
            int j = r.nextInt(i - 1) + 1;
            lisaServ(e, olemas, i, j, true);
        }
        while (e.size() < m) {
            int a = r.nextInt(n) + 1;
            int b = r.nextInt(n) + 1;
            if (a == b) continue;
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            lisaServ(e, olemas, a, b, true);
        }

        List<String> read = new ArrayList<>();
        read.add("p edge " + n + " " + m);
        for (int[] kaar : e) {
            int w = r.nextInt(maxKaal - minKaal + 1) + minKaal;
            read.add("e " + kaar[0] + " " + kaar[1] + " " + w);
        }
        return read;
    }

    private static List<String> genereeriEeldus(int n, int m, int minAeg, int maxAeg) {
        int minM = n - 1;
        int maxM = n * (n - 1) / 2;
        if (m < minM || m > maxM) {
            Teavitaja.teavita("Kaarte arv peab olema vahemikus " + minM + ".." + maxM, "Viga");
            return null;
        }

        Random r = new Random();
        Set<Long> olemas = new HashSet<>();
        List<int[]> e = new ArrayList<>();

        for (int i = 1; i < n; i++) lisaServ(e, olemas, i, i + 1, true);
        while (e.size() < m) {
            int a = r.nextInt(n) + 1;
            int b = r.nextInt(n) + 1;
            if (a == b) continue;
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            if (a == b) continue;
            lisaServ(e, olemas, a, b, true);
        }

        StringBuilder es = new StringBuilder("p edge ").append(n).append(" ").append(m);
        for (int i = 0; i < n; i++) es.append(" ").append(r.nextInt(maxAeg - minAeg + 1) + minAeg);

        List<String> read = new ArrayList<>();
        read.add(es.toString());
        for (int[] kaar : e) read.add("e " + kaar[0] + " " + kaar[1]);
        return read;
    }

    private static boolean lisaServ(List<int[]> e, Set<Long> olemas, int a, int b, boolean canonicalOrder) {
        if (canonicalOrder && a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        long key = (((long) a) << 32) | (b & 0xffffffffL);
        if (olemas.contains(key)) return false;
        olemas.add(key);
        e.add(new int[]{a, b});
        return true;
    }
}


