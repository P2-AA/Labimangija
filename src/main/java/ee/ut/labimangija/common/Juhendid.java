package ee.ut.labimangija.common;

public final class Juhendid {
    private Juhendid() {
    }

    public static String graaf(String key) {
        return switch (key) {
            case "laiuti" -> "Laiuti läbimine\n\nKasutatav andmestruktuur: järjekord\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Töötle selles järjlased (väljuvad kaared) ning siis märgi praegune tipp töödelduduks (klikates sellele).";
            case "sygavuti_ees" -> "Sügavuti läbimine (eesjärjestuses)\n\nKasutatav andmestruktuur: magasin\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Märgi praegune tipp töödelduduks ning seejärel töötle tema järjlased (väljuvad kaared).";
            case "sygavuti_lopp" -> "Sügavuti läbimine (lõppjärjestuses)\n\nKasutavadad andmestruktuurid: magasinid\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Töötle praeguse tipu järjlased (väljuvad kaared) ning seejärel märgi praegune tipp töödelduduks.";
            case "prim" -> "Minimaalse kaaluga toespuu leidmine (Prim)\n\nKasutatav andmestruktuur: eelistusjärjekord (kuhi - automaatne)\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Lisa kõik praegu töödeldava tipuga intsidentsed kaared eelistusjärjekorda, seejärel märgi tipp töödelduduks ning võta eelistusjärjekorrast uus tipp.";
            case "kruskal" -> "Minimaalse kaaluga toespuu leidmine (Kruskal)\n\nKasutatav andmestruktuur: kuhi (automaatne)\n\nKõik tipud on kohe rohelised (ühetipuliste puude mets). Hakka eelistusjärjekorrast kaari võtma ning iga kaare kohta otsusta, kas ta kuulub toesesse või mitte.";
            case "dijkstra" -> "Kauguste leidmine algtipust kõikidesse teistesse tippudesse\n\nKasutatav andmestruktuur: kuhi (automaatne)\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Töötle praeguse tipu järjlased (väljuvad kaared). Selleks vajuta kaare peale ning sisesta järjlase (uus) kaal. Seejärel märgi praegune tipp töödelduduks.\n\nLäbimängu lõpetamine: kui kõik tipud on töödeldud ja kuhi on saanud tühiaks, vajuta \"Võta\" nuppu.";
            case "fw" -> "Kauguste leidmine igast tipust igasse tippu\n\nKasutatav andmestruktuur: maatriks\n\nPunaseks värvidud tipp (üks rida ja üks veerg maatriksis) on fikseeritud. Vajuta halli nuppu maatriksis ning sisesta (uus) kaugus. Kui kogu maatriks (va punane rida ja veerg) on töödeldud, siis märgi praegune tipp töödelduduks.";
            case "bf" -> "Kauguste leidmine algtipust kõikidesse teistesse tippudesse\n\nKasutatav andmestruktuur: järjekord\n\nVõta järjekorrast serv ning töötle see, see tähendab sisesta lõpptipu (uus) kaugus. Järjekorras tähendab | uue ringi algust. Järjekorrast kriipsu võtmisel küsitakse, kas peaks algoritmi töö lõpetama.";
            case "kahn" -> "Topoloogilise järjestuse leidmine\n\nKasutatavad andmestruktuurid: paisktabel ja järjekord\n\nMääratud tabelis igale tipule tema sisendaste ning vajuta nuppu \"Kontrolli\". Kui sisendastmed on korrektsed, siis kaob nupp ära. Seejärel lisa tipud sisendastmega 0 järjekorda. Võta järjekorrast tipp, töötle väljuvad kaared (vähenda järglaste sisendastmeid), lisa sisendastmega 0 tipud järjekorda ja määratud tipp töödelduduks. Korda kuni kõik tipud on töödeldud.";
            case "eeldus" -> "Graafi eeldusanalüüs\n\nTipus kujutatud info vasakult paremale, ülevalt alla on tähis, tipu aeg, varaseim lõpp, hiliseim algus\n\nSisesta topoloogiline järjestus (tipu tähised komadega eraldatud), seejärel märgi igale tipule varaseim lõpuaeg, siis märgi kogu projekti (graafi) varaseim lõpuaeg, seejärel märgi igale tipule hiliseim algusaeg ja lõpuks sisesta kriitilised tipud.";
            default -> "";
        };
    }

    public static String kahendpuu(String key) {
        return switch (key) {
            case "jarjend_bst" -> "Kasutusjuhend:\n- Kontrolli lisamist: kontrollib kas tipp on lisatud korrektselt ja võtab järjendist uue tipu lisamiseks.\n- Eelmine puu olek: laeb viimati kontrollitud puu.\n- Klõps tipul: muudab tipu aktiivseks, võimaldab lisada uue alluva ja muuta tipu väärtust.";
            case "eemaldamine_bst" -> "Kasutusjuhend:\n- Kontrolli eemaldust: kontrollib tipu eemaldust ja võtab eemaldatavate järjendist uue eemaldatava.\n- Lae eelmine puu olek: laeb viimati kontrollitud puu.\n- Rohelise tipu kirjet saab muuta, kui ta on ainukene aktiivne tipp.\n- Lisa vasak/parem alluv: lisab punase tipu rohelise tipu vasakuks/paremaks alluvaks.";
            case "lisamine_avl" -> "Kasutusjuhend:\n- Kontrolli lisamist: kontrollib tipu lisamise sammu.\n- Lae eelmine puu olek: laeb viimati kontrollitud puu.\n- Eemalda seos alluvaga: eemaldab seose vasaku või parema alluvaga.\n- Lisa vasak/parem alluv: lisab punase tipu rohelise tipu vasakuks või paremaks alluvaks.";
            case "eemaldamine_avl" -> "Kasutusjuhend:\n- Lukusta puu olek: kontrollib tipu eemaldust.\n- Lae eelmine puu olek: laeb viimati lukustatud puu.\n- Eemalda seos alluvaga: eemaldab seose vasaku või parema alluvaga.\n- Lisa vasak/parem alluv: lisab punase tipu rohelise tipu vasakuks või paremaks alluvaks.";
            case "jarjendi_kuhjastamine" -> "Kasutusjuhend:\nLisa elemendid järjendist kompaktsesse kahendpuusse, seejärel kuhjasta kompaktne kahendpuu.\n- Kontrolli kuhjastatud puud: kontrollib kuhjastamise lõpptulemust.\n- Vaheta tipud: saab vahetada kahte tippu vastavalt kuhjastamise algoritmile.";
            case "kuhjameetod" -> "Kasutusjuhend:\nTipu asukohtade muutused kuvatakse massiivis.\n- Kontrolli sammu: kontrollib kuhjameetodi sammu. Samm on ühe elemendi töötlemine ja kuhja struktuuri taastamine.\n- Eelmine puu olek: laeb viimati kontrollitud kuhja.\n- Märgi element töödelduduks: lisab elemendi massiivi lõppu ja eemaldab kuhja struktuurist.\n- Vaheta tipud: vahetab omavahel kaks aktiivset tippu.";
            default -> "";
        };
    }

    public static String massiiv(String key) {
        return switch (key) {
            case "mullimeetod" -> "Mullimeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea vajadusel tööala, seejärel tee algoritmi sammud vastavate nuppudega. \"Piste\" on kasutusel mullimeetodi lähimuses, \"Lõpeta\" hindab lahenduse ja \"Tagasi\" võtab viimase sammu tagasi.";
            case "pistemeetod" -> "Pistemeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea tööala ning tee sammud indeksitega. \"Piste\" lisab elemendi õigesse kohta, \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab lähimuses.";
            case "valikumeetod" -> "Valikumeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea tööala ning kasuta \"Vaheta\" nuppu minimaalse elemendi õigesse kohta viimiseks. \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab lähimuses.";
            case "valiku_kiirmeetod" -> "Valiku kiirmeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea tööala, vajadusel sisesta jaotus väljal \"Jaota\" uus massiiv kasutades lahkmekohana sümbolit _. \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab lähimuses.";
            default -> "";
        };
    }

    public static String paisktabel(String key) {
        return switch (key) {
            case "lisamine" -> "Lisamine\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Seejärel täida väljad i, r ja vajadusel k ning kasuta \"Sisesta\" nuppu. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" hindab lahenduse.";
            case "eemaldamine" -> "Eemaldamine\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Seejärel täida väljad i, r ja vajadusel k ning kasuta \"Eemalda\" nuppu. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" hindab lahenduse.";
            case "kimp" -> "Kimbumeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Esmalt sisesta algseadistus kujul a b m, seejärel kasuta välju i, r ja vajadusel k vastavate sammude tegemiseks. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" hindab lahenduse.";
            case "positsioon" -> "Positsioonimeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Esmalt sisesta paisktabeli pikkus, seejärel kasuta välju i, r ja vajadusel k vastavate sammude tegemiseks. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" hindab lahenduse.";
            default -> "";
        };
    }
}
