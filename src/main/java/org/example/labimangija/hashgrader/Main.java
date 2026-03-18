package org.example.labimangija.hashgrader;

import java.io.IOException;
import java.util.Scanner;
import org.example.labimangija.hashgrader.samm.EemaldamiseSamm;
import org.example.labimangija.hashgrader.samm.LõpetamiseSamm;
import org.example.labimangija.hashgrader.samm.PaisktabeliLoomiseSamm;
import org.example.labimangija.hashgrader.samm.SisestamiseSamm;
import org.example.labimangija.hashgrader.ylesanne.EemaldamiseYlesanne;
import org.example.labimangija.hashgrader.ylesanne.KimbuYlesanne;
import org.example.labimangija.hashgrader.ylesanne.LisamiseYlesanne;
import org.example.labimangija.hashgrader.ylesanne.PositsiooniYlesanne;

public class Main {
    private static final String LISAMINE_EEMALDAMINE = "sisendid/lisamineEemaldamine/sisend.txt";
    private static final String KIMBU_MEETOD = "sisendid/kimbumeetod/sisend.txt";
    private static final String POSITSIOONI_MEETOD = "sisendid/positsioonimeetod/sisend.txt";

    public static void main(String[] args) throws IOException {
        Läbimäng läbimäng = new Läbimäng();
        läbimäng.setHindaja(new Hindaja());

        Scanner sc = new Scanner(System.in);
        boolean alusta = true;
        String[] userCommand;

        while (true) {
            if (alusta) {
                System.out.println("ALGUS");
                while (true) {
                    System.out.println("""
                            l - lisamine
                            e - eemaldamine
                            k - kimbumeetod
                            p - positsioonimeetod
                            x - välju""");

                    System.out.print("Vali ülesande tüüp: ");
                    String ylesandeTüüp = sc.nextLine();

                    switch (ylesandeTüüp) {
                        case "l" -> läbimäng.setYlesanne(new LisamiseYlesanne(LISAMINE_EEMALDAMINE));
                        case "e" -> läbimäng.setYlesanne(new EemaldamiseYlesanne(LISAMINE_EEMALDAMINE));
                        case "k" -> läbimäng.setYlesanne(new KimbuYlesanne(KIMBU_MEETOD));
                        case "p" -> läbimäng.setYlesanne(new PositsiooniYlesanne(POSITSIOONI_MEETOD));
                        case "x" -> {
                            return;
                        }
                        default -> {
                            continue;
                        }
                    }

                    System.out.println(läbimäng.ylesandeKirjeldus());

                    try {
                        switch (ylesandeTüüp) {
                            case "p" -> {
                                System.out.print("Sisesta paisktabeli pikkus: ");
                                userCommand = sc.nextLine().split(" ");
                                läbimäng.astu(new PaisktabeliLoomiseSamm(Integer.parseInt(userCommand[0])));
                            }
                            case "k" -> {
                                System.out.print("Sisesta a b m (eraldatud tühikutega): ");
                                userCommand = sc.nextLine().split(" ");
                                läbimäng.astu(new PaisktabeliLoomiseSamm(
                                        Float.parseFloat(userCommand[0]),
                                        Float.parseFloat(userCommand[1]),
                                        Integer.parseInt(userCommand[2])
                                ));
                            }
                            default -> {
                            }
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                alusta = false;
            }

            System.out.println("-----------------------------------------------------------");
            System.out.println("töödeldav alamjärjend: " + läbimäng.getAbijärjend());
            System.out.println("paisktabel:\n" + läbimäng.getPaisktabel());
            System.out.println("""
                    Vali käsk:
                    l - algoritm lõpetab
                    s <i> <r> (<k>) - sisesta element massiivist indeksilt i paisktabelisse reale r (kohale k)
                    e <i> <r> (<k>) - eemalda paisktabelist realt r (kohalt k) element ja pane see massiivi indeksile i
                    u - võta samm tagasi""");

            userCommand = sc.nextLine().split(" ");

            try {
                switch (userCommand[0]) {
                    case "l" -> {
                        läbimäng.astu(new LõpetamiseSamm());
                        System.out.println("Hinne: " + läbimäng.getPunktid() + "%");
                        alusta = true;
                    }
                    case "s" -> {
                        if (userCommand.length > 3) {
                            läbimäng.astu(new SisestamiseSamm(
                                    Integer.parseInt(userCommand[1]),
                                    Integer.parseInt(userCommand[2]),
                                    Integer.parseInt(userCommand[3])
                            ));
                        } else {
                            läbimäng.astu(new SisestamiseSamm(
                                    Integer.parseInt(userCommand[1]),
                                    Integer.parseInt(userCommand[2]),
                                    0
                            ));
                        }
                    }
                    case "e" -> {
                        if (userCommand.length > 3) {
                            läbimäng.astu(new EemaldamiseSamm(
                                    Integer.parseInt(userCommand[1]),
                                    Integer.parseInt(userCommand[2]),
                                    Integer.parseInt(userCommand[3])
                            ));
                        } else {
                            läbimäng.astu(new EemaldamiseSamm(
                                    Integer.parseInt(userCommand[1]),
                                    Integer.parseInt(userCommand[2]),
                                    0
                            ));
                        }
                    }
                    case "u" -> alusta = läbimäng.tagasi();
                    default -> System.out.println("Käsku ei leitud.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
