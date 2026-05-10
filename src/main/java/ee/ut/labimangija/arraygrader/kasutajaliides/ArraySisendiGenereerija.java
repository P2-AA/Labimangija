package ee.ut.labimangija.arraygrader.kasutajaliides;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class ArraySisendiGenereerija {
    private static final Random RANDOM = new Random();

    private record Parameetrid(int pikkus, int min, int max, int raskus) {}

    public static ArraySisendiValija.Sisend genereeriFail(ArrayGraderEngine.Algoritm algoritm) {
        Parameetrid vaike = vaikeParameetrid(algoritm);
        Parameetrid parameetrid = kysiParameetrid(algoritm, vaike);
        if (parameetrid == null) {
            return null;
        }

        try {
            int[] massiiv = genereeriMassiiv(algoritm, parameetrid);
            Path kaust = ArraySisendiValija.sisendiKaust(algoritm);
            Files.createDirectories(kaust);
            String aeg = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path fail = kaust.resolve("gen_" + aeg + ".txt");
            Files.writeString(fail, Arrays.toString(massiiv), StandardCharsets.UTF_8);
            return new ArraySisendiValija.Sisend(massiiv, "Genereeritud fail: " + fail.toAbsolutePath());
        } catch (Exception e) {
            naitaViga("Genereerimine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    private static Parameetrid vaikeParameetrid(ArrayGraderEngine.Algoritm algoritm) {
        return switch (algoritm) {
            case MULLIMEETOD -> new Parameetrid(5, 0, 19, 3);
            case PISTEMEETOD, VALIKUMEETOD -> new Parameetrid(5, 0, 19, 2);
            case VALIKU_KIIRMEETOD -> new Parameetrid(5, 0, 19, 2);
        };
    }

    private static Parameetrid kysiParameetrid(ArrayGraderEngine.Algoritm algoritm, Parameetrid vaike) {
        Dialog<Parameetrid> dialog = new Dialog<>();
        dialog.setTitle("Massiivi genereerimine");
        dialog.setHeaderText("Sisesta genereeritava massiivi parameetrid.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField pikkus = new TextField(String.valueOf(vaike.pikkus));
        TextField min = new TextField(String.valueOf(vaike.min));
        TextField max = new TextField(String.valueOf(vaike.max));
        TextField raskus = new TextField(String.valueOf(vaike.raskus));

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(new Label("Massiivi pikkus"), 0, 0);
        grid.add(pikkus, 1, 0);
        grid.add(new Label("Min väärtus"), 0, 1);
        grid.add(min, 1, 1);
        grid.add(new Label("Max väärtus"), 0, 2);
        grid.add(max, 1, 2);
        grid.add(new Label(raskuseLabel(algoritm)), 0, 3);
        grid.add(raskus, 1, 3);
        GridPane.setHgrow(pikkus, Priority.ALWAYS);
        GridPane.setHgrow(min, Priority.ALWAYS);
        GridPane.setHgrow(max, Priority.ALWAYS);
        GridPane.setHgrow(raskus, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            try {
                return new Parameetrid(
                        Integer.parseInt(pikkus.getText().trim()),
                        Integer.parseInt(min.getText().trim()),
                        Integer.parseInt(max.getText().trim()),
                        Integer.parseInt(raskus.getText().trim())
                );
            } catch (NumberFormatException e) {
                naitaViga("Kõik parameetrid peavad olema täisarvud.");
                return null;
            }
        });

        Optional<Parameetrid> tulemus = dialog.showAndWait();
        if (tulemus.isEmpty()) {
            return null;
        }

        Parameetrid parameetrid = tulemus.get();
        String viga = valideeri(algoritm, parameetrid);
        if (viga != null) {
            naitaViga(viga);
            return null;
        }
        return parameetrid;
    }

    private static String raskuseLabel(ArrayGraderEngine.Algoritm algoritm) {
        return switch (algoritm) {
            case MULLIMEETOD -> "Iteratsioonid";
            case PISTEMEETOD -> "Pisted";
            case VALIKUMEETOD -> "Vahetused";
            case VALIKU_KIIRMEETOD -> "Jaotamised";
        };
    }

    private static String valideeri(ArrayGraderEngine.Algoritm algoritm, Parameetrid p) {
        if (p.pikkus < 5) {
            return "Massiivi pikkus peab olema vähemalt 5.";
        }
        if (p.max < p.min) {
            return "Maksimum peab olema vähemalt miinimumiga võrdne.";
        }
        if (p.pikkus > p.max - p.min + 1) {
            return "Vahemikus peab olema piisavalt erinevaid väärtuseid.";
        }
        return switch (algoritm) {
            case MULLIMEETOD -> p.raskus < 1 || p.raskus > p.pikkus
                    ? "Iteratsioonide arv peab olema vahemikus 1 kuni massiivi pikkus." : null;
            case PISTEMEETOD -> p.raskus < 0 || p.raskus >= p.pikkus
                    ? "Pistete arv peab olema vahemikus 0 kuni massiivi pikkus - 1." : null;
            case VALIKUMEETOD -> p.raskus < 0 || p.raskus >= p.pikkus
                    ? "Vahetuste arv peab olema vahemikus 0 kuni massiivi pikkus - 1." : null;
            case VALIKU_KIIRMEETOD -> p.raskus < 1 || p.raskus >= p.pikkus
                    ? "Jaotamiste arv peab olema vahemikus 1 kuni massiivi pikkus - 1." : null;
        };
    }

    private static int[] genereeriMassiiv(ArrayGraderEngine.Algoritm algoritm, Parameetrid p) {
        int[] massiiv = RANDOM.ints(p.min, p.max + 1).distinct().limit(p.pikkus).sorted().toArray();
        switch (algoritm) {
            case MULLIMEETOD -> segaMullimeetodile(massiiv, p.raskus);
            case PISTEMEETOD -> segaPistemeetodile(massiiv, p.raskus);
            case VALIKUMEETOD -> segaValikumeetodile(massiiv, p.raskus);
            case VALIKU_KIIRMEETOD -> segaValikuKiirmeetodile(massiiv, p.raskus);
        }
        return massiiv;
    }

    private static void segaMullimeetodile(int[] massiiv, int iteratsioonid) {
        if (iteratsioonid == 1) {
            return;
        }
        int n = massiiv.length;
        int bound = n - iteratsioonid + 1;
        int vahetused = binomiaal(1, bound, 1.0 / iteratsioonid);
        for (int i = 0; i < n; i++) {
            int max = i + iteratsioonid - 1;
            int j;
            if (vahetused > 0 && RANDOM.nextInt(n - max) < vahetused) {
                j = max;
                vahetused--;
            } else {
                j = RANDOM.nextInt(i, Math.min(max, n));
            }
            vaheta(massiiv, i, j);
        }
    }

    private static void segaPistemeetodile(int[] massiiv, int pisted) {
        if (pisted == 0) {
            return;
        }
        for (int i = massiiv.length - 1; i > 0; i--) {
            if (RANDOM.nextInt(i) < pisted) {
                int j = RANDOM.nextInt(0, i);
                pista(massiiv, j, i);
                pisted--;
            }
        }
    }

    private static void segaValikumeetodile(int[] massiiv, int vahetused) {
        if (vahetused == 0) {
            return;
        }
        for (int i = massiiv.length - 1; i > 0; i--) {
            if (RANDOM.nextInt(i) < vahetused) {
                int j = RANDOM.nextInt(0, i);
                vaheta(massiiv, i, j);
                vahetused--;
            }
        }
    }

    private static void segaValikuKiirmeetodile(int[] massiiv, int jaotamised) {
        int k = Math.min(2, massiiv.length - 2);
        int katseid = 0;
        do {
            if (++katseid > 100_000) {
                throw new IllegalStateException("Sobiva valiku kiirmeetodi sisendi leidmine võttis liiga kaua aega.");
            }
            sega(massiiv);
        } while (loendaJaotamised(massiiv.clone(), k) != jaotamised);
    }

    private static int loendaJaotamised(int[] massiiv, int k) {
        return loendaJaotamised(massiiv, 0, massiiv.length - 1, k, 0);
    }

    private static int loendaJaotamised(int[] massiiv, int vasak, int parem, int k, int arv) {
        if (vasak == parem) {
            return arv;
        }
        int pi = jaota(massiiv, vasak, parem);
        arv++;
        if (pi == k) {
            return arv;
        }
        if (k < pi) {
            return loendaJaotamised(massiiv, vasak, pi, k, arv);
        }
        return loendaJaotamised(massiiv, pi + 1, parem, k, arv);
    }

    private static int jaota(int[] massiiv, int vasak, int parem) {
        int lahe = massiiv[vasak];
        int i = vasak - 1;
        int j = parem + 1;
        while (true) {
            do {
                i++;
            } while (massiiv[i] < lahe);
            do {
                j--;
            } while (massiiv[j] > lahe);
            if (i < j) {
                vaheta(massiiv, i, j);
            } else {
                return j;
            }
        }
    }

    private static void pista(int[] massiiv, int i, int j) {
        int x = massiiv[i];
        while (i < j) {
            massiiv[i] = massiiv[++i];
        }
        massiiv[j] = x;
    }

    private static void sega(int[] massiiv) {
        for (int i = massiiv.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            vaheta(massiiv, i, j);
        }
    }

    private static int binomiaal(int min, int n, double p) {
        int x = 0;
        while (x < min) {
            for (int i = 0; i < n; i++) {
                if (RANDOM.nextDouble() < p) {
                    x++;
                }
            }
        }
        return x;
    }

    private static void vaheta(int[] massiiv, int i, int j) {
        int ajutine = massiiv[i];
        massiiv[i] = massiiv[j];
        massiiv[j] = ajutine;
    }

    private static void naitaViga(String teade) {
        new Alert(Alert.AlertType.ERROR, teade, ButtonType.OK).showAndWait();
    }
}

