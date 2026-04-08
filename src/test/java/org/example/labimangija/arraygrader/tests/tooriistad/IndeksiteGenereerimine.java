package org.example.labimangija.arraygrader.tests.tooriistad;

import java.util.ArrayList;
import java.util.List;

public class IndeksiteGenereerimine {

    public static List<TööalaIndeksid> leiaKõikvõimalikudTööalaValimiseIndeksid(int massiiviPikkus) {
        List<TööalaIndeksid> indeksitePaarid = new ArrayList<>();

        // Tööala valimised eeldusega, et algusindeks < lõpuindeks
        for (int i = 0; i < massiiviPikkus; i++) {
            for (int j = i; j < massiiviPikkus; j++) {
                indeksitePaarid.add(new TööalaIndeksid(i, j + 1));
            }
        }
        return indeksitePaarid;
    }

    public static List<PisteIndeksid> leiaKõikVõimalikudPisteIndeksid(int massiiviPikkus) {
        List<PisteIndeksid> indeksitePaarid = new ArrayList<>();

        // Pisted eeldusega, et piste alg- ja lõpp-punkt on erinevad
        for (int i = 0; i < massiiviPikkus; i++) {
            for (int j = 0; j < massiiviPikkus; j++) {
                if (i != j) {
                    indeksitePaarid.add(new PisteIndeksid(i, j));
                }
            }
        }
        return indeksitePaarid;
    }

    public static List<VahetatavadIndeksid> leiaKõikVõimalikudVahetusteIndeksid(int massiiviPikkus) {
        List<VahetatavadIndeksid> indeksitePaarid = new ArrayList<>();

        // Elementide vahetused eeldusega, et vahetatavad elemendid on erinevad.
        // Elementide järjekorral vahetuses pole vahet, nii et võtan ainult juhud, kus esimene on väiksem
        for (int i = 0; i < massiiviPikkus; i++) {
            for (int j = i + 1; j < massiiviPikkus; j++) {
                indeksitePaarid.add(new VahetatavadIndeksid(i, j));
            }
        }
        return indeksitePaarid;
    }

    public record TööalaIndeksid(int algus, int lõpustJärgmine) {
    }

    public record PisteIndeksid(int algus, int lõpp) {
    }

    public record VahetatavadIndeksid(int vahetatav1, int vahetatav2) {
    }

}
