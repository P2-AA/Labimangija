package ee.ut.labimangija.arraygrader.kasutajaliides;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import ee.ut.labimangija.arraygrader.ArrayGraderLogija;
import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

public class ArrayGraderEngine {
    public enum Algoritm {
        MULLIMEETOD("Mullimeetod"),
        PISTEMEETOD("Pistemeetod"),
        VALIKUMEETOD("Valikumeetod"),
        VALIKU_KIIRMEETOD("Valiku kiirmeetod");

        private final String pealkiri;

        Algoritm(String pealkiri) {
            this.pealkiri = pealkiri;
        }

        public String getPealkiri() {
            return pealkiri;
        }
    }

    private static final int MASSIIVI_PIKKUS = 5;
    private static final int MAX_JUHUSLIK_VAARTUS = 20;

    private final Random random = new Random();

    private Algoritm algoritm = Algoritm.MULLIMEETOD;
    private Kasutajaliides kasutajaliides;
    private List<Massiivioperatsioon> kaigud = new ArrayList<>();
    private String viimaneTeade = "Vali algoritm ja alusta uut läbimängu.";
    private boolean lopetatud;
    private ArrayGraderLogija logija;
    private String sisendiKirjeldus = "Juhuslik massiiv";
    private int vigadeArv;

    public void alusta(Algoritm uusAlgoritm) {
        alusta(uusAlgoritm, looMassiiv(), "Juhuslik massiiv");
    }

    public void alusta(Algoritm uusAlgoritm, int[] massiiv, String uusSisendiKirjeldus) {
        algoritm = uusAlgoritm;
        kasutajaliides = looKasutajaliides(uusAlgoritm);
        kaigud = new ArrayList<>();
        lopetatud = false;
        sisendiKirjeldus = uusSisendiKirjeldus == null ? "Sisend" : uusSisendiKirjeldus;
        logija = new ArrayGraderLogija(uusAlgoritm.getPealkiri(), "Harjutamine");
        vigadeArv = 0;

        Massiivioperatsioon algus = kasutajaliides
                .läbimänguAlustamiseOperatsioon(Arrays.copyOf(massiiv, massiiv.length));
        kaigud.add(algus);
        logiAlgus();

        viimaneTeade = "Läbimäng algas.";
        logiStaatus();
    }

    public void executeCommand(String command) {
        String sisend = command == null ? "" : command.trim();
        if (sisend.isEmpty()) {
            throw new IllegalArgumentException("Vali tegevus.");
        }
        if (!onAktiivne()) {
            throw new IllegalArgumentException("Alusta enne uut läbimängu.");
        }
        if (lopetatud) {
            throw new IllegalArgumentException("Läbimäng on juba lõpetatud. Alusta uut läbimängu.");
        }

        String[] osad = sisend.split("\\s+");
        String kask = osad[0].toLowerCase(Locale.ROOT);

        switch (kask) {
            case "tagasi" -> votaTagasi();
            case "lopeta", "lõpeta" -> lopeta();
            default -> teeSamm(osad);
        }

        logija.logi("KÄSK: " + sisend);
        logiStaatus();
    }

    public String render() {
        StringBuilder sb = new StringBuilder();

        if (!onAktiivne()) {
            sb.append("Vali ülevalt algoritm ja vajuta \"Alusta\".");
            return sb.toString();
        }

        sb.append("Sisend: ").append(sisendiKirjeldus).append("\n\n");
        sb.append(kirjeldus()).append("\n\n");
        sb.append("Praegune seis: ").append(getPraeguneKaik().getSeis()).append("\n");

        if (getPraeguneKaik().getSeis() instanceof ValikuKiirmeetodiMassiiviSeis kiireSeis) {
            sb.append("Vastuse piir: ").append(kiireSeis.getVastusePiir()).append("\n");
        }

        sb.append("\nKäikude ajalugu:\n");
        for (int i = 0; i < kaigud.size(); i++) {
            sb.append(i).append(". ").append(kaigud.get(i)).append("\n");
        }

        return sb.toString().trim();
    }

    public String getViimaneTeade() {
        return viimaneTeade;
    }

    public boolean onAktiivne() {
        return !kaigud.isEmpty();
    }

    public boolean onMuudetav() {
        return onAktiivne() && !lopetatud;
    }

    public boolean supportsPiste() {
        return algoritm == Algoritm.MULLIMEETOD || algoritm == Algoritm.PISTEMEETOD;
    }

    public boolean supportsVaheta() {
        return algoritm == Algoritm.VALIKUMEETOD;
    }

    public boolean supportsJaota() {
        return algoritm == Algoritm.VALIKU_KIIRMEETOD;
    }

    private void teeSamm(String[] osad) {
        Massiivioperatsioon eelmineKaik = getPraeguneKaik();
        Massiivioperatsioon uusKaik;

        try {
            uusKaik = kasutajaliides.leiaOperatsioon(osad, eelmineKaik.getSeis());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        Massiivioperatsioon oigeKaik = eelmineKaik.järgmineÕigeKäik();
        if (!uusKaik.equals(oigeKaik)) {
            vigadeArv++;
            viimaneTeade = "Vale käik.";
            logija.logi("VALE KÄSK: " + uusKaik);
            logija.logi("ÕIGE JARGMINE KÄSK: " + oigeKaik);
            return;
        }

        kaigud.add(uusKaik);
        viimaneTeade = "Õige käik.";
        logija.logi("SAMM: " + uusKaik);
    }

    private void votaTagasi() {
        if (kaigud.size() <= 1) {
            viimaneTeade = "Rohkem käike ei saa tagasi võtta.";
            logija.logi("TAGASI: ebaõnnestus, rohkem käike ei ole.");
            return;
        }

        Massiivioperatsioon eemaldatudKaik = kaigud.remove(kaigud.size() - 1);
        viimaneTeade = "Viimane käik võeti tagasi.";
        logija.logi("TAGASI: eemaldati " + eemaldatudKaik);
    }

    private void lopeta() {
        Massiivioperatsioon loppKaik = new LäbimänguLõpetamine(getPraeguneKaik().getSeis());

        Massiivioperatsioon oigeKaik = getPraeguneKaik().järgmineÕigeKäik();
        if (!loppKaik.equals(oigeKaik)) {
            viimaneTeade = "Praegu ei saa veel lõpetada.";
            logija.logi("LÕPETAMINE LÜKATI TAGASI");
            logija.logi("ÕIGE JARGMINE KÄSK: " + oigeKaik);
            return;
        }

        kaigud.add(loppKaik);
        lopetatud = true;
        viimaneTeade = "Palju õnne! Harjutus sai lõpetatud. Vigu: " + vigadeArv;
        logija.logi("LÕPP: harjutus sai lõpetatud.");
        logija.logi("VIGADE ARV: " + vigadeArv);
        logiKaigud();
    }

    private Massiivioperatsioon getPraeguneKaik() {
        return kaigud.get(kaigud.size() - 1);
    }

    private Kasutajaliides looKasutajaliides(Algoritm algoritm) {
        return switch (algoritm) {
            case MULLIMEETOD -> new MullimeetodiKasutajaliides();
            case PISTEMEETOD -> new PistemeetodiKasutajaliides();
            case VALIKUMEETOD -> new ValikumeetodiKasutajaliides();
            case VALIKU_KIIRMEETOD -> new ValikuKiirmeetodiKasutajaliides();
        };
    }

    private int[] looMassiiv() {
        int[] massiiv = new int[MASSIIVI_PIKKUS];
        for (int i = 0; i < massiiv.length; i++) {
            massiiv[i] = random.nextInt(MAX_JUHUSLIK_VAARTUS);
        }
        return massiiv;
    }

    private String kirjeldus() {
        int[] massiiv = getPraeguneKaik().getSeis().getMassiiv();
        return switch (algoritm) {
            case MULLIMEETOD ->
                "Alustame mullimeetodi läbimängu massiivil " + Arrays.toString(massiiv)
                        + ". Kasutame varianti, " + "\n"
                        + "kus massiiv loetakse sorteerituks, kui ühtegi vahetust ei tehta.";
            case PISTEMEETOD ->
                "Alustame pistemeetodi läbimängu massiivil " + Arrays.toString(massiiv) + ".";
            case VALIKUMEETOD ->
                "Alustame valikumeetodi läbimängu massiivil " + Arrays.toString(massiiv) + ".";
            case VALIKU_KIIRMEETOD ->
                "Alustame valiku kiirmeetodi läbimängu massiivil " + Arrays.toString(massiiv)
                        + ", tuua esimesed 3 elementi massiivi algusesse.";
        };
    }

    private void logiAlgus() {
        logija.logi("ALGUS");
        logija.logi("Algoritm: " + algoritm.getPealkiri());
        logija.logi("Läbimäng: Harjutamine");
        logija.logi("Sisend: " + sisendiKirjeldus);
        logija.logi(kirjeldus());
    }

    private void logiStaatus() {
        if (logija == null || kaigud.isEmpty()) {
            return;
        }
        logija.logi("PRAEGUNE SEIS: " + getPraeguneKaik().getSeis());
        logija.logi("STAATUS: " + viimaneTeade);
        logija.logi("--------------------------------------------------");
    }

    private void logiKaigud() {
        logija.logi("KÄIKUDE AJALUGU");
        for (int i = 0; i < kaigud.size(); i++) {
            logija.logi(i + ". " + kaigud.get(i));
        }
        logija.logi("==================================================");
    }
}
