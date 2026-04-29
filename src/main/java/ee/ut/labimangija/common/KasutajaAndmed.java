package ee.ut.labimangija.common;

import java.util.prefs.Preferences;

public final class KasutajaAndmed {
    private static final Preferences EELISTUSED = Preferences.userNodeForPackage(KasutajaAndmed.class);
    private static final String NIMI_VOTI = "kasutaja.nimi";
    private static final String MATRIKLI_VOTI = "kasutaja.matrikkel";

    private KasutajaAndmed() {
    }

    public static String getNimi() {
        return EELISTUSED.get(NIMI_VOTI, "");
    }

    public static void setNimi(String nimi) {
        EELISTUSED.put(NIMI_VOTI, ohutu(nimi));
    }

    public static String getMatrikkel() {
        return EELISTUSED.get(MATRIKLI_VOTI, "");
    }

    public static void setMatrikkel(String matrikkel) {
        EELISTUSED.put(MATRIKLI_VOTI, ohutu(matrikkel));
    }

    public static String logiPais() {
        return "Nimi: " + vaartusVoiPuudub(getNimi()) + System.lineSeparator()
                + "Matrikkel: " + vaartusVoiPuudub(getMatrikkel()) + System.lineSeparator();
    }

    private static String ohutu(String vaartus) {
        return vaartus == null ? "" : vaartus.trim();
    }

    private static String vaartusVoiPuudub(String vaartus) {
        return vaartus == null || vaartus.isBlank() ? "-" : vaartus;
    }
}
