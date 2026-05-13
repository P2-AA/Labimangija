package ee.ut.labimangija.common;

public final class Juhendid {
    private Juhendid() {
    }

    public static String graaf(String key) {
        return switch (key) {
            case "laiuti" ->
                "Laiuti läbimine\n\nKasutatav andmestruktuur: järjekord\n\nPunaseks värvitud tipp on aktiivne tipp.\nTöötle aktiivse tipu järglased ning siis märgi aktiivne tipp töödelduks.";

            case "sygavuti_ees" ->
                "Sügavuti läbimine (eesjärjestuses)\n\nKasutatav andmestruktuur: magasin\n\nPunaseks värvitud tipp on aktiivne tipp. Töötle hetkel aktiivse tipu järglased ja seejärel määra see tipp töödelduks.";

            case "sygavuti_lopp" ->
                "Sügavuti läbimine (lõppjärjestuses)\n\nKasutav andmestruktuur: magasin\n\nPunaseks värvitud tipp on aktiivne tipp. Töötle aktiivse tipu järglased (väljuvate kaarte lõpptipud) ning seejärel märgi hetkel aktiivne tipp töödelduks.";

            case "prim" ->
                "Minimaalse kaaluga toespuu leidmine (Prim)\n\nKasutatav andmestruktuur: eelistusjärjekord\n\nPunaseks värvitud tipp on aktiivne tipp. Lisa kõik hetkel töödeldava tipuga intsidentsed kaared eelistusjärjekorda, seejärel märgi tipp töödelduduks ning võta eelistusjärjekorrast uus tipp.";

            case "kruskal" ->
                "Minimaalse kaaluga toespuu leidmine (Kruskal)\n\nKasutatav andmestruktuur: kuhi\n\nKõik tipud on kohe töödeldud. Hakka eelistusjärjekorrast kaari võtma ning iga kaare kohta otsusta, kas ta kuulub toesesse või mitte.";

            case "dijkstra" ->
                "Kauguste leidmine algtipust kõikidesse teistesse tippudesse\n\nKasutatav andmestruktuur: kuhi \n\nPunaseks värvitud tipp on aktiivne tipp. Töötle aktiivse tipu järglased. Selleks vajuta kaare peale ning sisesta järglase (uus) kaal. Seejärel märgi aktiivne tipp töödelduduks.\n\nLäbimängu lõpetamine: kui kõik tipud on töödeldud ja kuhi on saanud tühjaks, vajuta \"Võta\" nuppu.";

            case "fw" ->
                "Kauguste leidmine igast tipust igasse tippu\n\nKasutatav andmestruktuur: maatriks\n\nPunaseks värvidud tipp (üks rida ja üks veerg maatriksis) on fikseeritud. Vajuta halli nuppu maatriksis ning sisesta (uus) kaugus. Kui kogu maatriks (va punane rida ja veerg) on töödeldud, siis märgi aktiivne tipp töödelduks.";

            case "bf" ->
                "Kauguste leidmine algtipust kõikidesse teistesse tippudesse\n\nKasutatav andmestruktuur: järjekord\n\nVõta järjekorrast serv ning töötle see, see tähendab sisesta lõpptipu (uus) kaugus. Järjekorras tähendab | uue ringi algust. Järjekorrast kriipsu võtmisel küsitakse, kas peaks algoritmi töö lõpetama.";

            case "kahn" ->
                "Topoloogilise järjestuse leidmine\n\nKasutatavad andmestruktuurid: paisktabel ja järjekord\n\nMääratud tabelis igale tipule tema sisendaste ning vajuta nuppu \"Kontrolli\". Kui sisendastmed on korrektsed, siis kaob nupp ära. Seejärel lisa tipud sisendastmega 0 järjekorda. Võta järjekorrast tipp, töötle väljuvad kaared, lisa sisendastmega 0 tipud järjekorda ja määra tipp töödelduks. Parempoolses paneelis olevad nupud \"lisa\", tähendavad tipu töösse lisamist";

            case "eeldus" ->
                "Graafi eeldusanalüüs\n\nSisesta topoloogiline järjestus. Seejärel klõpsa aktiivse tipu alumisel vasakul väljal ning määra varaseim lõpp, tee seda kõigi tippude puhul. Kui tipud on töödeldud, siis sisesta graafi varaseim lõpuaeg. Seejärel klõpsa aktiivse tipu alumisel paremal väljal ning sisesta hiliseim algusaeg. Lõpus sisesta kriitiline(sed) tipp(tipud)";

            default -> "";
        };
    }

    public static String kahendpuu(String key) {
        return switch (key) {
            case "jarjend_bst" ->
                "Kasutusjuhend:\n- Kontrolli lisamist: kontrollib kas tipp on lisatud korrektselt ja võtab järjendist uue tipu lisamiseks.\n- Eelmine puu olek: laeb viimati kontrollitud puu.\n- Klõps tipul: muudab tipu aktiivseks, võimaldab lisada uue alluva ja muuta tipu väärtust. Aktiivse tipu väärtust saab muuta, kasutades backspace-i ning kirjutades uus väärtus asemele.";
            case "eemaldamine_bst" ->
                "Kasutusjuhend:\n- Kontrolli eemaldust: kontrollib tipu eemaldust ja võtab eemaldatavate järjendist uue eemaldatava.\n- Lae eelmine puu olek: laeb viimati kontrollitud puu.\n- Rohelise tipu kirjet saab muuta, kui ta on ainukene aktiivne tipp.\n- Lisa vasak/parem alluv: lisab punase tipu rohelise tipu vasakuks/paremaks alluvaks. Aktiivse tipu väärtust saab muuta, kasutades backspace-i ning kirjutades uus väärtus asemele.";
            case "lisamine_avl" ->
                "Kasutusjuhend:\n- Kontrolli lisamist: kontrollib tipu lisamise sammu.\n- Lae eelmine puu olek: laeb viimati kontrollitud puu.\n- Eemalda seos alluvaga: eemaldab seose vasaku või parema alluvaga.\n- Lisa vasak/parem alluv: lisab punase tipu rohelise tipu vasakuks või paremaks alluvaks. Aktiivse tipu väärtust saab muuta, kasutades backspace-i ning kirjutades uus väärtus asemele.";
            case "eemaldamine_avl" ->
                "Kasutusjuhend:\n- Lukusta puu olek: kontrollib tipu eemaldust.\n- Lae eelmine puu olek: laeb viimati lukustatud puu.\n- Eemalda seos alluvaga: eemaldab seose vasaku või parema alluvaga.\n- Lisa vasak/parem alluv: lisab punase tipu rohelise tipu vasakuks või paremaks alluvaks. Aktiivse tipu väärtust saab muuta, kasutades backspace-i ning kirjutades uus väärtus asemele.";
            case "jarjendi_kuhjastamine" ->
                "Kasutusjuhend:\nLisa elemendid järjendist kompaktsesse kahendpuusse, seejärel kuhjasta kompaktne kahendpuu.\n- Kontrolli kuhjastatud puud: kontrollib kuhjastamise lõpptulemust.\n- Vaheta tipud: saab vahetada kahte tippu vastavalt kuhjastamise algoritmile. Aktiivse tipu väärtust saab muuta, kasutades backspace-i ning kirjutades uus väärtus asemele.";
            case "kuhjameetod" ->
                "Kasutusjuhend:\nTipu asukohtade muutused kuvatakse massiivis.\n- Kontrolli sammu: kontrollib kuhjameetodi sammu. Samm on ühe elemendi töötlemine ja kuhja struktuuri taastamine.\n- Eelmine puu olek: laeb viimati kontrollitud kuhja.\n- Märgi element töödelduduks: lisab elemendi massiivi lõppu ja eemaldab kuhja struktuurist.\n- Vaheta tipud: vahetab omavahel kaks aktiivset tippu. Aktiivse tipu väärtust saab muuta, kasutades backspace-i ning kirjutades uus väärtus asemele.";
            default -> "";
        };
    }

    public static String massiiv(String key) {
        return switch (key) {
            case "mullimeetod" ->
                "Mullimeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea vajadusel tööala, seejärel kasuta \"Vaheta\" nuppu kõrvutiasetsevate elementide vahetamiseks. Mullimeetodis tehakse vahetusi ainult naaberelementide vahel. \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab läbimängu.";
            case "pistemeetod" ->
                "Pistemeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea tööala ning tee sammud indeksitega. \"Piste\" lisab elemendi õigesse kohta, \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab läbimängu.";
            case "valikumeetod" ->
                "Valikumeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea tööala ning kasuta \"Vaheta\" nuppu minimaalse elemendi õigesse kohta viimiseks. \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab läbimängu.";
            case "valiku_kiirmeetod" ->
                "Valiku kiirmeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Sea tööala, vajadusel sisesta jaotus väljal \"Jaota\" uus massiiv kasutades lahkmekohana sümbolit _. \"Tagasi\" võtab viimase sammu tagasi ja \"Lõpeta\" lõpetab läbimängu.";
            default -> "";
        };
    }

    public static String paisktabel(String key) {
        return switch (key) {
            case "lisamine" ->
                "Lisamine\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Seejärel sisesta paisktabeli rea number, kuhu järjekordne element lisatakse, ja kasuta \"Sisesta\" nuppu. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" lõpetab läbimängu.";
            case "eemaldamine" ->
                "Eemaldamine\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Seejärel täida väljad i = alammassiivi indeks, r = paisktabeli rida ja vajadusel k = koht ning kasuta \"Eemalda\" nuppu. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" lõpetab läbimängu.";
            case "kimp" ->
                "Kimbumeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Esmalt sisesta algseadistus kujul a = minimaalne element, b = maksimaalne element ja m = kimpude arv, seejärel kasuta välju i = alammassiivi indeks, r = paisktabeli rida ja vajadusel k = koht vastavate sammude tegemiseks. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" lõpetab läbimängu.";
            case "positsioon" ->
                "Positsioonimeetod\n\nKasuta \"Alusta\" nuppu, et laadida uus ülesanne. Esmalt sisesta paisktabeli pikkus algseadistuse väljale. Seejärel jaota elemendid paisktabeli ridadele, korja read samas järjekorras tagasi kokku ning jätka jaotamisega. \"Võta tagasi\" tühistab viimase sammu ja \"Lõpeta\" lõpetab läbimängu.";
            default -> "";
        };
    }
}
