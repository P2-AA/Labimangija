package org.example.labimangija.arraygrader.kasutajaliides;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ArraySisendiValija {
    private static final Path SISENDI_JUUR = Path.of(System.getProperty("user.dir"), "sisendid", "massiivialgoritmid");
    private static final Pattern TAISARV = Pattern.compile("-?\\d+");
    private static final Random RANDOM = new Random();

    public record Sisend(int[] massiiv, String kirjeldus) {}

    public static Sisend valiSisend(ArrayGraderEngine.Algoritm algoritm) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Massiivi sisend");
        dialog.setHeaderText("Vali läbimängu algmassiivi allikas.");

        ButtonType genereeri = new ButtonType("Genereeri fail");
        ButtonType valiFail = new ButtonType("Vali fail");
        ButtonType suvalineFail = new ButtonType("Suvaline olemasolev");
        ButtonType katkesta = new ButtonType("Katkesta", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(genereeri, valiFail, suvalineFail, katkesta);

        Optional<ButtonType> valik = dialog.showAndWait();
        if (valik.isEmpty() || valik.get() == katkesta) {
            return null;
        }
        if (valik.get() == genereeri) {
            return ArraySisendiGenereerija.genereeriFail(algoritm);
        }
        if (valik.get() == suvalineFail) {
            return valiSuvalineFail(algoritm);
        }
        return valiFail(algoritm);
    }

    static Path sisendiKaust(ArrayGraderEngine.Algoritm algoritm) {
        return SISENDI_JUUR.resolve(switch (algoritm) {
            case MULLIMEETOD -> "mullimeetod";
            case PISTEMEETOD -> "pistemeetod";
            case VALIKUMEETOD -> "valikumeetod";
            case VALIKU_KIIRMEETOD -> "valiku_kiirmeetod";
        });
    }

    private static Sisend valiFail(ArrayGraderEngine.Algoritm algoritm) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali massiivialgoritmi sisendfail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        Path kaust = sisendiKaust(algoritm);
        if (Files.isDirectory(kaust)) {
            chooser.setInitialDirectory(kaust.toFile());
        }

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) {
            return null;
        }
        return loeFail(valitud.toPath());
    }

    private static Sisend valiSuvalineFail(ArrayGraderEngine.Algoritm algoritm) {
        Path kaust = sisendiKaust(algoritm);
        if (!Files.isDirectory(kaust)) {
            naitaViga("Selle algoritmi jaoks ei ole veel sisendikausta: " + kaust.toAbsolutePath());
            return null;
        }

        try {
            List<Path> failid;
            try (Stream<Path> kaustaFailid = Files.list(kaust)) {
                failid = kaustaFailid
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".txt"))
                        .toList();
            }
            if (failid.isEmpty()) {
                naitaViga("Selle algoritmi jaoks ei ole olemasolevaid sisendfaile.");
                return null;
            }
            return loeFail(failid.get(RANDOM.nextInt(failid.size())));
        } catch (IOException e) {
            naitaViga("Sisendfaili valimine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    private static Sisend loeFail(Path fail) {
        try {
            List<String> read = Files.readAllLines(fail, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(rida -> !rida.isEmpty())
                    .toList();
            if (read.isEmpty()) {
                naitaViga("Sisendfail on tühi.");
                return null;
            }
            int[] massiiv = parsiMassiiv(read.get(0));
            if (massiiv.length < 5) {
                naitaViga("Sisendfailis peab olema vähemalt viis arvu.");
                return null;
            }
            return new Sisend(massiiv, "Fail: " + fail.toAbsolutePath());
        } catch (IOException e) {
            naitaViga("Sisendfaili lugemine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    private static int[] parsiMassiiv(String rida) {
        Matcher matcher = TAISARV.matcher(rida);
        return matcher.results()
                .map(match -> match.group())
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static void naitaViga(String teade) {
        new Alert(Alert.AlertType.ERROR, teade, ButtonType.OK).showAndWait();
    }
}
