package ee.ut.labimangija.algorithmgrader.Util;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public final class AlgorithmSisendiGenereerija {
    private static final Random RANDOM = new Random();

    private AlgorithmSisendiGenereerija() {
    }

    private record Parameetrid(int elemente, int tegevusi, int min, int max) {
    }

    private static final class IntNode {
        private final int value;
        private final IntNode left;
        private final IntNode right;

        private IntNode(int value, IntNode left, IntNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    public static String genereeriFail(AlgorithmSisendiValija.Tyyp tyyp) {
        Parameetrid parameetrid = kysiParameetrid(tyyp);
        if (parameetrid == null) {
            return null;
        }

        try {
            String sisu = switch (tyyp) {
                case BST_LISAMINE -> genereeriBstLisamine(parameetrid);
                case BST_EEMALDAMINE -> genereeriBstEemaldamine(parameetrid);
                case AVL_LISAMINE -> genereeriAvlLisamine(parameetrid);
                case AVL_EEMALDAMINE -> genereeriAvlEemaldamine(parameetrid);
                case KUHJASTAMINE -> genereeriMassiiv(parameetrid.elemente(), parameetrid.min(), parameetrid.max());
                case KUHJAMEETOD -> genereeriMassiiv(parameetrid.elemente(), parameetrid.min(), parameetrid.max());
            };

            Path kaust = AlgorithmSisendiValija.sisendiKaust();
            Files.createDirectories(kaust);
            String aeg = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nimi = "gen_" + tyyp.failiPrefix() + "_" + aeg + ".txt";
            Path fail = kaust.resolve(nimi);
            Files.writeString(fail, sisu + System.lineSeparator(), StandardCharsets.UTF_8);
            return fail.toAbsolutePath().toString();
        } catch (Exception e) {
            AlgorithmSisendiValija.naitaViga("Genereerimine ebaonnestus: " + e.getMessage());
            return null;
        }
    }

    private static Parameetrid kysiParameetrid(AlgorithmSisendiValija.Tyyp tyyp) {
        Parameetrid vaike = vaikeParameetrid(tyyp);

        Dialog<Parameetrid> dialog = new Dialog<>();
        dialog.setTitle("Algorithmgraderi sisendi genereerimine");
        dialog.setHeaderText("Sisesta genereeritava sisendi parameetrid.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField elementeField = new TextField(String.valueOf(vaike.elemente()));
        TextField tegevusiField = new TextField(String.valueOf(vaike.tegevusi()));
        TextField minField = new TextField(String.valueOf(vaike.min()));
        TextField maxField = new TextField(String.valueOf(vaike.max()));

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        int rida = 0;
        rida = lisaRida(grid, rida, "Elementide arv", elementeField);
        if (vajabTegevusteArvu(tyyp)) {
            rida = lisaRida(grid, rida, tegevuseSilt(tyyp), tegevusiField);
        }
        rida = lisaRida(grid, rida, "Min vaartus", minField);
        lisaRida(grid, rida, "Max vaartus", maxField);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            try {
                return new Parameetrid(
                        Integer.parseInt(elementeField.getText().trim()),
                        Integer.parseInt(tegevusiField.getText().trim()),
                        Integer.parseInt(minField.getText().trim()),
                        Integer.parseInt(maxField.getText().trim())
                );
            } catch (NumberFormatException e) {
                AlgorithmSisendiValija.naitaViga("Koik parameetrid peavad olema taisarvud.");
                return null;
            }
        });

        Optional<Parameetrid> tulemus = dialog.showAndWait();
        if (tulemus.isEmpty()) {
            return null;
        }

        String viga = valideeri(tyyp, tulemus.get());
        if (viga != null) {
            AlgorithmSisendiValija.naitaViga(viga);
            return null;
        }
        return tulemus.get();
    }

    private static Parameetrid vaikeParameetrid(AlgorithmSisendiValija.Tyyp tyyp) {
        return switch (tyyp) {
            case BST_LISAMINE -> new Parameetrid(11, 0, 1, 99);
            case BST_EEMALDAMINE -> new Parameetrid(12, 2, 1, 99);
            case AVL_LISAMINE -> new Parameetrid(10, 3, 1, 99);
            case AVL_EEMALDAMINE -> new Parameetrid(15, 2, 1, 99);
            case KUHJASTAMINE -> new Parameetrid(7, 0, 1, 99);
            case KUHJAMEETOD -> new Parameetrid(7, 0, 1, 99);
        };
    }

    private static boolean vajabTegevusteArvu(AlgorithmSisendiValija.Tyyp tyyp) {
        return switch (tyyp) {
            case BST_EEMALDAMINE, AVL_LISAMINE, AVL_EEMALDAMINE -> true;
            default -> false;
        };
    }

    private static String tegevuseSilt(AlgorithmSisendiValija.Tyyp tyyp) {
        return switch (tyyp) {
            case BST_EEMALDAMINE, AVL_EEMALDAMINE -> "Eemaldatavate arv";
            case AVL_LISAMINE -> "Lisatavate arv";
            default -> "Tegevuste arv";
        };
    }

    private static int lisaRida(GridPane grid, int rida, String silt, TextField field) {
        grid.add(new Label(silt), 0, rida);
        grid.add(field, 1, rida);
        GridPane.setHgrow(field, Priority.ALWAYS);
        return rida + 1;
    }

    private static String valideeri(AlgorithmSisendiValija.Tyyp tyyp, Parameetrid p) {
        if (p.elemente() < 1) {
            return "Elementide arv peab olema positiivne.";
        }
        if (p.max() <= p.min()) {
            return "Max vaartus peab olema min vaartusest suurem.";
        }
        int vahemik = p.max() - p.min() + 1;
        if (vahemik < p.elemente()) {
            return "Antud vahemik ei sisalda piisavalt erinevaid vaartusi.";
        }
        if (vajabTegevusteArvu(tyyp) && p.tegevusi() < 1) {
            return "Tegevuste arv peab olema vahemalt 1.";
        }
        if ((tyyp == AlgorithmSisendiValija.Tyyp.BST_EEMALDAMINE
                || tyyp == AlgorithmSisendiValija.Tyyp.AVL_EEMALDAMINE)
                && p.tegevusi() >= p.elemente()) {
            return "Eemaldatavate arv peab olema vaiksem kui elementide arv.";
        }
        if (tyyp == AlgorithmSisendiValija.Tyyp.AVL_LISAMINE && vahemik < p.elemente() + p.tegevusi()) {
            return "AVL lisamise jaoks peab vahemikus olema ruumi nii puule kui lisatavatele elementidele.";
        }
        return null;
    }

    private static String genereeriBstLisamine(Parameetrid p) {
        return vormindaMassiiv(juhuslikudErinevadArvud(p.elemente(), p.min(), p.max()));
    }

    private static String genereeriBstEemaldamine(Parameetrid p) {
        List<Integer> puu = juhuslikudErinevadArvud(p.elemente(), p.min(), p.max());
        List<Integer> eemaldatavad = juhuslikAlamhulk(puu, p.tegevusi());
        return "Kahendotsimispuu jarjend: " + vormindaMassiiv(puu)
                + "      Eemaldatavad elemendid: " + vormindaMassiiv(eemaldatavad);
    }

    private static String genereeriAvlLisamine(Parameetrid p) {
        List<Integer> koik = juhuslikudErinevadArvud(p.elemente() + p.tegevusi(), p.min(), p.max());
        Collections.sort(koik);
        List<Integer> alus = new ArrayList<>(koik.subList(0, p.elemente()));
        List<Integer> lisatavad = new ArrayList<>(koik.subList(p.elemente(), koik.size()));
        Collections.shuffle(lisatavad, RANDOM);
        return "AVL-puu jarjend: " + vormindaMassiiv(alus)
                + "      Lisatavad elemendid: " + vormindaMassiiv(lisatavad);
    }

    private static String genereeriAvlEemaldamine(Parameetrid p) {
        List<Integer> sorteeritud = juhuslikudErinevadArvud(p.elemente(), p.min(), p.max());
        Collections.sort(sorteeritud);
        IntNode juur = tasakaalustatudPuu(sorteeritud, 0, sorteeritud.size() - 1);
        List<Integer> puuJarjend = tasemeJarjekord(juur);
        List<Integer> eemaldatavad = juhuslikAlamhulk(puuJarjend, p.tegevusi());
        return "AVL-puu jarjend: " + vormindaMassiiv(puuJarjend)
                + "      Eemaldatavad elemendid: " + vormindaMassiiv(eemaldatavad);
    }

    private static String genereeriMassiiv(int elemente, int min, int max) {
        return vormindaMassiiv(juhuslikudErinevadArvud(elemente, min, max));
    }

    private static List<Integer> juhuslikudErinevadArvud(int kogus, int min, int max) {
        List<Integer> koik = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            koik.add(i);
        }
        Collections.shuffle(koik, RANDOM);
        List<Integer> vastus = new ArrayList<>(koik.subList(0, kogus));
        Collections.shuffle(vastus, RANDOM);
        return vastus;
    }

    private static List<Integer> juhuslikAlamhulk(List<Integer> alus, int kogus) {
        List<Integer> koopia = new ArrayList<>(alus);
        Collections.shuffle(koopia, RANDOM);
        return new ArrayList<>(koopia.subList(0, kogus));
    }

    private static String vormindaMassiiv(List<Integer> arvud) {
        return "[" + arvud.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]";
    }

    private static IntNode tasakaalustatudPuu(List<Integer> sorteeritud, int algus, int lopp) {
        if (algus > lopp) {
            return null;
        }
        int kesk = (algus + lopp) / 2;
        return new IntNode(
                sorteeritud.get(kesk),
                tasakaalustatudPuu(sorteeritud, algus, kesk - 1),
                tasakaalustatudPuu(sorteeritud, kesk + 1, lopp)
        );
    }

    private static List<Integer> tasemeJarjekord(IntNode juur) {
        List<Integer> vastus = new ArrayList<>();
        if (juur == null) {
            return vastus;
        }
        ArrayDeque<IntNode> jarjekord = new ArrayDeque<>();
        jarjekord.add(juur);
        while (!jarjekord.isEmpty()) {
            IntNode praegune = jarjekord.removeFirst();
            vastus.add(praegune.value);
            if (praegune.left != null) {
                jarjekord.addLast(praegune.left);
            }
            if (praegune.right != null) {
                jarjekord.addLast(praegune.right);
            }
        }
        return vastus;
    }
}
