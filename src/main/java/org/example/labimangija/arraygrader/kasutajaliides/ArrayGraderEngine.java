package org.example.labimangija.arraygrader.kasutajaliides;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.example.labimangija.arraygrader.ArrayGraderLogija;
import org.example.labimangija.arraygrader.MassiiviTööriistad;
import org.example.labimangija.arraygrader.labimanguhindaja.Hindamistulemus;
import org.example.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import org.example.labimangija.arraygrader.massiiviseis.ValikuKiirmeetodiMassiiviSeis;
import org.example.labimangija.arraygrader.massiivioperatsioon.LäbimänguLõpetamine;
import org.example.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

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

    public enum Reziim {
        HARJUTAMINE("Harjutamine"),
        KONTROLLTOO("Kontrolltöö"),
        NAIDE("Näide");

        private final String pealkiri;

        Reziim(String pealkiri) {
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
    private Reziim reziim = Reziim.HARJUTAMINE;
    private Kasutajaliides kasutajaliides;
    private List<Massiivioperatsioon> kaigud = new ArrayList<>();
    private String viimaneTeade = "Vali algoritm ja režiim ning alusta uut läbimängu.";
    private boolean lopetatud;
    private ArrayGraderLogija logija;

    public void alusta(Algoritm uusAlgoritm, Reziim uusReziim) {
        algoritm = uusAlgoritm;
        reziim = uusReziim;
        kasutajaliides = looKasutajaliides(uusAlgoritm);
        kaigud = new ArrayList<>();
        lopetatud = false;
        logija = new ArrayGraderLogija(uusAlgoritm.getPealkiri(), uusReziim.getPealkiri());

        Massiivioperatsioon algus = kasutajaliides.läbimänguAlustamiseOperatsioon(looMassiiv());
        kaigud.add(algus);
        logiAlgus();

        if (reziim == Reziim.NAIDE) {
            kaigud = MassiiviTööriistad.kopeeriKäigudJajätkaLäbimängu(new ArrayList<>(), algus);
            lopetatud = true;
            viimaneTeade = "Näitelahendus genereeriti.";
            logija.logi("NÄITE REZIIMI TÄISLAHENDUS");
            logiKaigud();
        } else {
            viimaneTeade = "Läbimäng algas.";
        }
        logiStaatus();
    }

    public void executeCommand(String command) {
        String sisend = command == null ? "" : command.trim();
        if (sisend.isEmpty()) {
            throw new IllegalArgumentException("Sisesta käsk.");
        }
        if (!onAktiivne()) {
            throw new IllegalArgumentException("Alusta enne uut läbimängu.");
        }
        if (reziim == Reziim.NAIDE) {
            throw new IllegalArgumentException("Näiterežiimis ei saa käike sisestada.");
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
        sb.append(algoritm.getPealkiri()).append("\n");
        sb.append("Režiim: ").append(reziim.getPealkiri()).append("\n\n");

        if (!onAktiivne()) {
            sb.append("Läbimäng pole veel alanud.\n");
            sb.append("Vali ülevalt algoritm, vali režiim ja vajuta \"Alusta\".");
            return sb.toString();
        }

        sb.append(kirjeldus()).append("\n\n");
        sb.append("Praegune seis: ").append(getPraeguneKaik().getSeis()).append("\n");

        if (getPraeguneKaik().getSeis() instanceof ValikuKiirmeetodiMassiiviSeis kiireSeis) {
            sb.append("Vastuse piir: ").append(kiireSeis.getVastusePiir()).append("\n");
        }

        sb.append("\nKäikude ajalugu:\n");
        for (int i = 0; i < kaigud.size(); i++) {
            sb.append(i).append(". ").append(kaigud.get(i)).append("\n");
        }

        if (reziim != Reziim.NAIDE) {
            sb.append("\nVõimalikud käsud:\n");
            for (String rida : abiRead()) {
                sb.append(rida).append("\n");
            }
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
        return onAktiivne() && reziim != Reziim.NAIDE && !lopetatud;
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

        if (reziim == Reziim.HARJUTAMINE) {
            Massiivioperatsioon oigeKaik = eelmineKaik.järgmineÕigeKäik();
            if (!uusKaik.equals(oigeKaik)) {
                viimaneTeade = "Vale käik. Õige järgmine käik oleks olnud: " + oigeKaik;
                logija.logi("VALE KÄSK: " + uusKaik);
                logija.logi("ÕIGE JARGMINE KÄSK: " + oigeKaik);
                return;
            }
        }

        kaigud.add(uusKaik);
        viimaneTeade = reziim == Reziim.HARJUTAMINE ? "Õige käik." : "Käik salvestati.";
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

        if (reziim == Reziim.HARJUTAMINE) {
            Massiivioperatsioon oigeKaik = getPraeguneKaik().järgmineÕigeKäik();
            if (!loppKaik.equals(oigeKaik)) {
                viimaneTeade = "Praegu ei saa veel lõpetada. Õige järgmine käik oleks: " + oigeKaik;
                logija.logi("LÕPETAMINE LÜKATI TAGASI");
                logija.logi("ÕIGE JARGMINE KÄSK: " + oigeKaik);
                return;
            }

            kaigud.add(loppKaik);
            lopetatud = true;
            viimaneTeade = "Palju õnne! Harjutus sai korrektselt lõpetatud.";
            logija.logi("LÕPP: harjutus sai korrektselt lõpetatud.");
            logiKaigud();
            return;
        }

        kaigud.add(loppKaik);
        lopetatud = true;

        Hindamistulemus tulemus = kasutajaliides.läbimänguHindaja().hinda(kaigud);
        viimaneTeade = "Tulemus: " + tulemus;
        logija.logi("LÕPPHINNANG: " + tulemus);
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
                            + ". Kasutame varianti, " + "\n" +"kus massiiv loetakse sorteerituks, kui ühtegi vahetust ei tehta.";
            case PISTEMEETOD ->
                    "Alustame pistemeetodi läbimängu massiivil " + Arrays.toString(massiiv) + ".";
            case VALIKUMEETOD ->
                    "Alustame valikumeetodi läbimängu massiivil " + Arrays.toString(massiiv) + ".";
            case VALIKU_KIIRMEETOD ->
                    "Alustame valiku kiirmeetodi läbimängu massiivil " + Arrays.toString(massiiv)
                            + ", tuua esimesed 3 elementi massiivi algusesse.";
        };
    }

    private List<String> abiRead() {
        List<String> read = new ArrayList<>();
        read.add("tööala <algusindeks> <lõpuindeks> - muudab tööala, lõpuindeks on tööalast välja arvatud");
        if (supportsPiste()) {
            read.add("piste <algusindeks> <lõpuindeks> - teeb massiivil piste");
        }
        if (supportsVaheta()) {
            read.add("vaheta <indeks1> <indeks2> - vahetab kaks elementi");
        }
        if (supportsJaota()) {
            read.add("jaota a b _ c d - kirjuta uus jaotus, alakriips märgib lahkmekohta");
        }
        read.add("tagasi - võtab viimase käigu tagasi");
        read.add("lõpeta - lõpetab läbimängu");
        return read;
    }

    private void logiAlgus() {
        logija.logi("ALGUS");
        logija.logi("Algoritm: " + algoritm.getPealkiri());
        logija.logi("Reziim: " + reziim.getPealkiri());
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
