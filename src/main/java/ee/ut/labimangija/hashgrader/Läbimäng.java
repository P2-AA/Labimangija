package ee.ut.labimangija.hashgrader;

import java.util.ArrayList;
import java.util.Stack;
import ee.ut.labimangija.hashgrader.samm.Samm;
import ee.ut.labimangija.hashgrader.ylesanne.Ylesanne;
import ee.ut.labimangija.hashgrader.ylesanne.LisamiseYlesanne;
import ee.ut.labimangija.hashgrader.ylesanne.EemaldamiseYlesanne;
import ee.ut.labimangija.hashgrader.ylesanne.KimbuYlesanne;
import ee.ut.labimangija.hashgrader.ylesanne.PositsiooniYlesanne;

public class Läbimäng<T> {

    private Stack<Hinnang> läbimäng;
    private Ylesanne<T> ylesanne;
    private Paisktabel<T> paisktabel;
    private ArrayList<T> abijärjend;
    private ArrayList<Hinnang> õigeLäbimäng;
    private Logija logija;
    private Hindaja hindaja;
    private float punktid;

    public void setYlesanne(Ylesanne<T> ylesanne) {
        this.läbimäng = new Stack<>();
        this.ylesanne = ylesanne;
        this.paisktabel = ylesanne.getPaisktabel();
        this.abijärjend = ylesanne.getAbijärjend();
        this.õigeLäbimäng = ylesanne.leiaÕigeLäbimäng();
        this.logija = new Logija(algoritmiNimi(ylesanne));
        logija.logi(ylesanne.ylesandeKirjeldus());
    }

    public void setHindaja(Hindaja hindaja) {
        this.hindaja = hindaja;
    }

    public void setPaisktabeliParameetrid(float minElem, float maxElem, int elementideArv) {
        ylesanne.setPaisktabeliParameetrid(minElem, maxElem, elementideArv);
    }

    public Paisktabel<T> getPaisktabel() {
        return paisktabel;
    }

    public ArrayList<T> getAbijärjend() {
        return abijärjend;
    }

    public float getPunktid() {
        return punktid;
    }

    public String algoritmiNimi(Ylesanne<T> ylesanne) {
        if (ylesanne instanceof LisamiseYlesanne) return "Lisamine";
        if (ylesanne instanceof EemaldamiseYlesanne) return "Eemaldamine";
        if (ylesanne instanceof KimbuYlesanne) return "Kimbumeetod";
        if (ylesanne instanceof PositsiooniYlesanne) return "Positsioonimeetod";
        return "HashGrader";
    }

    public boolean astu(Samm samm) {
        Hinnang hinnang = ylesanne.hindaSammu(samm, abijärjend, paisktabel);
        läbimäng.push(hinnang);

        logija.logi(abijärjend + "\n"
                + paisktabel
                + "\n---------------------------------------------------------\n"
                + hinnang);

        if (samm.astu(this)) {
            ylesanne.astu(this, hinnang);
            return true;
        }

        logija.logi("   VIGANE KÄSK");
        läbimäng.pop();
        return false;
    }

    public boolean tagasi() {
        if (läbimäng.isEmpty()) {
            return true;
        }

        Hinnang hinnang = läbimäng.pop();
        if (hinnang.tudengiSamm.tagasi(this)) {
            ylesanne.tagasi(this, hinnang);
            logija.logi(abijärjend + "\n"
                    + paisktabel
                    + "\n---------------------------------------------------------\n"
                    + "\nTAGASI:\n"
                    + hinnang);
        } else {
            läbimäng.push(hinnang);
        }

        return false;
    }

    public void lõpeta() {
        punktid = hindaja.arvutaHinne(läbimäng, õigeLäbimäng);
        logija.logi("###########################\nHinne: " + punktid + "%\n");
    }

    public String ylesandeKirjeldus() {
        return ylesanne.ylesandeKirjeldus();
    }
}

