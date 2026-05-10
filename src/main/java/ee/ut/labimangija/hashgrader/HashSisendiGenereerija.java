package ee.ut.labimangija.hashgrader;

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
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HashSisendiGenereerija {
    private static final Random RANDOM = new Random();

    private record Parameetrid(int count, int min, int max, int base, int shifts, int relocations,
            int steps, int emptyBuckets, int longestBranchSize) {
    }

    private record Element(int value, int modulus, int position, int shift) {
    }

    public static String genereeriFail(String tyyp) {
        Parameetrid vaike = vaikeParameetrid(tyyp);
        Parameetrid parameetrid = kysiParameetrid(tyyp, vaike);
        if (parameetrid == null) {
            return null;
        }

        try {
            String sisu = switch (tyyp) {
                case "l" -> genereeriLisamine(parameetrid);
                case "e" -> genereeriEemaldamine(parameetrid);
                case "k" -> genereeriKimbumeetod(parameetrid);
                case "p" -> genereeriPositsioonimeetod(parameetrid);
                default -> throw new IllegalArgumentException("Tundmatu paisktabeli ülesande tüüp: " + tyyp);
            };

            Path kaust = HashSisendiValija.sisendiKaust(tyyp);
            Files.createDirectories(kaust);
            String aeg = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path fail = kaust.resolve("gen_" + aeg + ".txt");
            Files.writeString(fail, sisu + System.lineSeparator(), StandardCharsets.UTF_8);
            return fail.toAbsolutePath().toString();
        } catch (Exception e) {
            HashSisendiValija.naitaViga("Genereerimine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    private static Parameetrid vaikeParameetrid(String tyyp) {
        return switch (tyyp) {
            case "l" -> new Parameetrid(10, 0, 20, 10, 3, 1, 3, 2, 3);
            case "e" -> new Parameetrid(10, 0, 20, 10, 3, 1, 3, 2, 3);
            case "k" -> new Parameetrid(10, 0, 20, 10, 3, 1, 3, 2, 3);
            case "p" -> new Parameetrid(10, 0, 100, 10, 3, 1, 2, 2, 3);
            default -> throw new IllegalArgumentException("Tundmatu paisktabeli ülesande tüüp: " + tyyp);
        };
    }

    private static Parameetrid kysiParameetrid(String tyyp, Parameetrid vaike) {
        Dialog<Parameetrid> dialog = new Dialog<>();
        dialog.setTitle("Paisktabeli sisendi genereerimine");
        dialog.setHeaderText("Sisesta genereeritava sisendi parameetrid.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField count = new TextField(String.valueOf(vaike.count));
        TextField min = new TextField(String.valueOf(vaike.min));
        TextField max = new TextField(String.valueOf(vaike.max));
        TextField base = new TextField(String.valueOf(vaike.base));
        TextField shifts = new TextField(String.valueOf(vaike.shifts));
        TextField relocations = new TextField(String.valueOf(vaike.relocations));
        TextField steps = new TextField(String.valueOf(vaike.steps));
        TextField emptyBuckets = new TextField(String.valueOf(vaike.emptyBuckets));
        TextField longestBranch = new TextField(String.valueOf(vaike.longestBranchSize));

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        int rida = 0;
        rida = lisaRida(grid, rida, "Elementide arv", count);
        rida = lisaRida(grid, rida, "Min väärtus", min);
        rida = lisaRida(grid, rida, "Max väärtus", max);
        if ("l".equals(tyyp) || "e".equals(tyyp)) {
            rida = lisaRida(grid, rida, "Paisktabeli ridu", base);
            rida = lisaRida(grid, rida, "Kompesammud lisamisel", shifts);
        }
        if ("e".equals(tyyp)) {
            rida = lisaRida(grid, rida, "Ümbertõstmised kustutamisel", relocations);
        }
        if ("k".equals(tyyp)) {
            rida = lisaRida(grid, rida, "Tühjad kimbud", emptyBuckets);
        }
        if ("p".equals(tyyp)) {
            rida = lisaRida(grid, rida, "Alus", base);
            rida = lisaRida(grid, rida, "Järke", steps);
            lisaRida(grid, rida, "Pikima kimbu suurus", longestBranch);
        }

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            try {
                return new Parameetrid(
                        Integer.parseInt(count.getText().trim()),
                        Integer.parseInt(min.getText().trim()),
                        Integer.parseInt(max.getText().trim()),
                        Integer.parseInt(base.getText().trim()),
                        Integer.parseInt(shifts.getText().trim()),
                        Integer.parseInt(relocations.getText().trim()),
                        Integer.parseInt(steps.getText().trim()),
                        Integer.parseInt(emptyBuckets.getText().trim()),
                        Integer.parseInt(longestBranch.getText().trim()));
            } catch (NumberFormatException e) {
                HashSisendiValija.naitaViga("Kõik parameetrid peavad olema täisarvud.");
                return null;
            }
        });

        Optional<Parameetrid> tulemus = dialog.showAndWait();
        if (tulemus.isEmpty()) {
            return null;
        }
        String viga = valideeri(tyyp, tulemus.get());
        if (viga != null) {
            HashSisendiValija.naitaViga(viga);
            return null;
        }
        return tulemus.get();
    }

    private static int lisaRida(GridPane grid, int rida, String silt, TextField field) {
        grid.add(new Label(silt), 0, rida);
        grid.add(field, 1, rida);
        GridPane.setHgrow(field, Priority.ALWAYS);
        return rida + 1;
    }

    private static String valideeri(String tyyp, Parameetrid p) {
        if (p.count <= 1) {
            return "Elementide arv peab olema suurem kui 1.";
        }
        if (p.max <= p.min) {
            return "Max väärtus peab olema min väärtusest suurem.";
        }
        if (p.max - p.min < p.count) {
            return "Väärtuste vahemik peab sisaldama piisavalt erinevaid arve.";
        }
        if (("l".equals(tyyp) || "e".equals(tyyp)) && p.shifts < 0) {
            return "Kompesammude arv ei tohi olla negatiivne.";
        }
        if ("e".equals(tyyp) && p.relocations < 0) {
            return "Ümbertõstmiste arv ei tohi olla negatiivne.";
        }
        if ("k".equals(tyyp) && (p.emptyBuckets < 0 || p.emptyBuckets >= p.count - 1)) {
            return "Tühjade kimpude arv peab olema vahemikus 0 kuni elementide arv - 2.";
        }
        if ("p".equals(tyyp)
                && (p.base < 2 || p.steps < 1 || p.longestBranchSize < 1 || p.longestBranchSize > p.count)) {
            return "Positsioonimeetodi alus peab olema vähemalt 2, järke vähemalt 1 ja pikim kimp sobivas vahemikus.";
        }
        return null;
    }

    private static String genereeriLisamine(Parameetrid p) {
        List<Element> elemendid = leiaLisamiseElemendid(p);
        return vormindaIntRida(elemendid.stream().map(Element::value).toList(), null, p.base());
    }

    private static String genereeriEemaldamine(Parameetrid p) {
        long algus = System.currentTimeMillis();
        while (System.currentTimeMillis() - algus <= 10_000L) {
            List<Element> lisatud = leiaLisamiseElemendid(p);
            List<Integer> jarjekord = IntStream.range(0, lisatud.size()).boxed()
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(jarjekord, RANDOM);
            for (int indeks : jarjekord) {
                int eemaldatav = lisatud.get(indeks).value();
                List<Element> alles = lisatud.stream()
                        .filter(element -> element.value() != eemaldatav)
                        .toList();
                List<Element> parast = paigutaElemendid(alles.stream().map(Element::value).toList(), p.base());
                if (loendaYmbertostmised(lisatud, parast) == p.relocations()) {
                    return vormindaIntRida(lisatud.stream().map(Element::value).toList(), eemaldatav, p.base());
                }
            }
        }
        throw new IllegalStateException("Sobivat eemaldamise sisendit ei leitud 10 sekundi jooksul.");
    }

    private static List<Element> leiaLisamiseElemendid(Parameetrid p) {
        long algus = System.currentTimeMillis();
        while (System.currentTimeMillis() - algus <= 1_000L) {
            List<Integer> vaartused = RANDOM.ints(p.min(), p.max()).distinct().limit(p.count()).boxed().toList();
            List<Element> elemendid = paigutaElemendid(vaartused, p.base());
            int nihked = elemendid.stream().mapToInt(Element::shift).sum();
            if (nihked == p.shifts()) {
                return elemendid;
            }
        }
        throw new IllegalStateException("Sobivat lisamise sisendit ei leitud. Proovi muuta ridu või kompesamme.");
    }

    private static List<Element> paigutaElemendid(List<Integer> vaartused, int base) {
        List<Element> elemendid = new ArrayList<>();
        Set<Integer> vabad = IntStream.range(0, base).boxed().collect(Collectors.toCollection(HashSet::new));
        for (int vaartus : vaartused) {
            int modulus = Math.floorMod(vaartus, base);
            int position = modulus;
            int shift = 0;
            while (!vabad.remove(position)) {
                position++;
                if (position == base) {
                    position = 0;
                }
                shift++;
            }
            elemendid.add(new Element(vaartus, modulus, position, shift));
        }
        return elemendid;
    }

    private static long loendaYmbertostmised(List<Element> enne, List<Element> parast) {
        Map<Integer, Integer> enneAsukohad = enne.stream().collect(Collectors.toMap(Element::value, Element::position));
        return parast.stream()
                .filter(element -> !enneAsukohad.get(element.value()).equals(element.position()))
                .count();
    }

    private static String genereeriKimbumeetod(Parameetrid p) {
        int rangeLimit = p.max() - p.min();
        List<Integer> dividers = new ArrayList<>(List.of(1, 2, 5, 10, 20));
        dividers.removeIf(divider -> p.count() * divider > rangeLimit);
        if (dividers.isEmpty()) {
            throw new IllegalArgumentException("Kimbumeetodi jaoks on väärtuste vahemik liiga väike.");
        }
        int divider = dividers.get(RANDOM.nextInt(dividers.size()));
        int range = p.count() * divider;
        int start = RANDOM.nextInt(rangeLimit - range + 1) + p.min();
        int end = start + range;

        List<Integer> emptyBuckets = RANDOM.ints(1, p.count() - 1).distinct().limit(p.emptyBuckets()).boxed().toList();
        List<Integer> buckets = IntStream.range(0, p.count())
                .filter(bucket -> !emptyBuckets.contains(bucket))
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Map<Integer, Integer> bucketToElementsAmount = new HashMap<>();
        while (buckets.size() < p.count()) {
            int bucket = RANDOM.nextInt(p.count());
            if (emptyBuckets.contains(bucket) || bucketToElementsAmount.merge(bucket, 1, Integer::sum) > 9) {
                continue;
            }
            buckets.add(bucket);
        }

        List<Double> elemendid = new ArrayList<>();
        boolean headNeeded = true;
        boolean tailNeeded = true;
        for (int bucket : buckets) {
            double element;
            do {
                double fraction = RANDOM.nextInt(10) / 10.0;
                if (headNeeded && bucket == 0) {
                    headNeeded = false;
                    element = start + fraction;
                } else if (tailNeeded && bucket == p.count() - 1) {
                    tailNeeded = false;
                    element = end - 1 + fraction;
                } else {
                    element = start + bucket * divider + RANDOM.nextInt(divider) + fraction;
                }
            } while (elemendid.contains(element));
            elemendid.add(element);
        }
        Collections.shuffle(elemendid, RANDOM);
        return "[" + elemendid.stream().map(String::valueOf).collect(Collectors.joining(" ")) + "]";
    }

    private static String genereeriPositsioonimeetod(Parameetrid p) {
        int start = (int) Math.pow(p.base(), p.steps() - 1);
        int end = (int) Math.pow(p.base(), p.steps());
        if (end <= start || end > p.max()) {
            end = p.max();
        }
        if (end - p.min() < p.count()) {
            throw new IllegalArgumentException(
                    "Positsioonimeetodi parameetritega ei ole piisavalt erinevaid väärtuseid.");
        }

        int randomStep = RANDOM.nextInt(p.steps());
        int divider = (int) Math.pow(p.base(), randomStep);
        int randomModulus = RANDOM.nextInt(p.base());
        Map<String, Integer> branchSizes = new HashMap<>();
        List<Integer> elemendid = new ArrayList<>();

        int katseid = 0;
        while (elemendid.size() != p.count()) {
            if (++katseid > 100_000) {
                throw new IllegalStateException("Sobivat positsioonimeetodi sisendit ei leitud.");
            }
            int element = elemendid.size() == p.longestBranchSize()
                    ? RANDOM.nextInt(Math.max(1, end - start)) + start
                    : RANDOM.nextInt(p.max() - p.min()) + p.min();
            boolean pikimaKimbuElement = elemendid.size() < p.longestBranchSize();
            if ((pikimaKimbuElement && element / divider % p.base() != randomModulus) || elemendid.contains(element)) {
                continue;
            }

            int longestStep = pikimaKimbuElement ? randomStep : p.steps();
            List<String> keys = leiaVotmed(p, element, branchSizes, longestStep);
            if (keys.isEmpty()) {
                continue;
            }
            keys.forEach(key -> branchSizes.merge(key, 1, Integer::sum));
            elemendid.add(element);
        }
        Collections.shuffle(elemendid, RANDOM);
        return vormindaIntRida(elemendid, null);
    }

    private static List<String> leiaVotmed(Parameetrid p, int element, Map<String, Integer> branchSizes,
            int longestStep) {
        List<String> keys = new ArrayList<>();
        for (int step = 0; step < p.steps(); step++) {
            int divider = (int) Math.pow(p.base(), step);
            int modulus = element / divider % p.base();
            String key = step + "_" + modulus;
            int piir = step == longestStep ? p.longestBranchSize() : p.longestBranchSize() - 1;
            if (branchSizes.getOrDefault(key, 0) < piir) {
                keys.add(key);
            } else {
                return new ArrayList<>();
            }
        }
        return keys;
    }

    private static String vormindaIntRida(List<Integer> vaartused, Integer tärniga) {
        return "[" + vaartused.stream()
                .map(value -> tärniga != null && value.equals(tärniga) ? value + "*" : String.valueOf(value))
                .collect(Collectors.joining(" ")) + "]";
    }

    private static String vormindaIntRida(List<Integer> vaartused, Integer tärniga, int ridu) {
        return "m=" + ridu + " " + vormindaIntRida(vaartused, tärniga);
    }
}
