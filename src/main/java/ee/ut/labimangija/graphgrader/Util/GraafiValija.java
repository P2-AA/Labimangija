package ee.ut.labimangija.graphgrader.Util;

import ee.ut.labimangija.ui.SisendiAllikaDialoog;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import ee.ut.labimangija.common.AppPaths;

// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7

public class GraafiValija {
    public static String valiFailVoiGenereeri(String kaust, GraafiGenereerija.Tyyp tyyp) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva("Graafi sisend");
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) return null;
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) return GraafiGenereerija.genereeriFail(tyyp, kaust);
        return valiFail(kaust);
    }

    private static String valiFail(String kaust) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vali graafifail");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tekstifailid", "*.txt"));
        Path kaustaTee = AppPaths.resolve(kaust);
        if (Files.isDirectory(kaustaTee)) chooser.setInitialDirectory(kaustaTee.toFile());

        File valitud = chooser.showOpenDialog(null);
        if (valitud == null) return null;
        return valitud.getAbsolutePath();
    }
}
