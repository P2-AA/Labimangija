package org.example.labimangija.hashgrader;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class HashSisendiValija {
    private static final Path SISENDI_JUUR = Path.of(System.getProperty("user.dir"), "sisendid", "paisktabelid");
    private static final Random RANDOM = new Random();

    public static String valiSisend(String tyyp) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Paisktabeli sisend");
        dialog.setHeaderText("Vali, kas sisend lugeda failist või genereerida.");

        ButtonType genereeri = new ButtonType("Genereeri");
        ButtonType valiFail = new ButtonType("Vali fail");
        ButtonType suvalineFail = new ButtonType("Suvaline olemasolev");
        ButtonType katkesta = new ButtonType("Katkesta", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(genereeri, valiFail, suvalineFail, katkesta);

        Optional<ButtonType> valik = dialog.showAndWait();
        if (valik.isEmpty() || valik.get() == katkesta) {
            return null;
        }
        if (valik.get() == genereeri) {
            return HashSisendiGenereerija.genereeriFail(tyyp);
        }
        if (valik.get() == suvalineFail) {
            return valiSuvalineFail(tyyp);
        }
        return valiFail(tyyp);
    }

    static Path sisendiKaust(String tyyp) {
        return SISENDI_JUUR.resolve(switch (tyyp) {
            case "l" -> "lisamine";
            case "e" -> "eemaldamine";
            case "k" -> "kimbumeetod";
            case "p" -> "positsioonimeetod";
            default -> throw new IllegalArgumentException("Tundmatu paisktabeli ülesande tüüp: " + tyyp);
        });
    }

    private static String valiFail(String tyyp) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali paisktabeli sisendfail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        Path kaust = sisendiKaust(tyyp);
        if (Files.isDirectory(kaust)) {
            chooser.setInitialDirectory(kaust.toFile());
        }

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) {
            return null;
        }
        return valitud.getAbsolutePath();
    }

    private static String valiSuvalineFail(String tyyp) {
        Path kaust = sisendiKaust(tyyp);
        if (!Files.isDirectory(kaust)) {
            naitaViga("Selle ülesande jaoks ei ole veel sisendikausta: " + kaust.toAbsolutePath());
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
                naitaViga("Selle ülesande jaoks ei ole olemasolevaid sisendfaile.");
                return null;
            }
            return failid.get(RANDOM.nextInt(failid.size())).toAbsolutePath().toString();
        } catch (IOException e) {
            naitaViga("Sisendfaili valimine ebaõnnestus: " + e.getMessage());
            return null;
        }
    }

    static void naitaViga(String teade) {
        new Alert(Alert.AlertType.ERROR, teade, ButtonType.OK).showAndWait();
    }
}
