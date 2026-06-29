package ee.ut.labimangija.treeheapgrader.Util;

import ee.ut.labimangija.common.AppPaths;
import ee.ut.labimangija.ui.SisendiAllikaDialoog;

import java.nio.file.Path;

public final class AlgorithmSisendiValija {
    private static final Path SISENDI_KAUST = AppPaths.resolve("sisendid", "kahendpuud_kuhjad");

    private AlgorithmSisendiValija() {
    }

    public enum Tyyp {
        BST_LISAMINE,
        BST_EEMALDAMINE,
        AVL_LISAMINE,
        AVL_EEMALDAMINE,
        KUHJASTAMINE,
        KUHJAMEETOD
    }

    public static String valiSisend(Tyyp tyyp) {
        SisendiAllikaDialoog.Valik valik = SisendiAllikaDialoog.kuva();
        if (valik == SisendiAllikaDialoog.Valik.KATKESTA) return null;
        if (valik == SisendiAllikaDialoog.Valik.GENEREERI) return AlgorithmSisendiGenereerija.genereeriFail(tyyp);
        return SisendiAllikaDialoog.valiFail(sisendiKaust(tyyp));
    }

    static Path sisendiKaust(Tyyp tyyp) {
        return SISENDI_KAUST.resolve(tyyp.name().toLowerCase());
    }

}
