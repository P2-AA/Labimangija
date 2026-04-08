package org.example.labimangija.arraygrader.labimanguhindaja;

public class Hindamistulemus {

    private int valedeKäikudeArv = 0;
    private int õigeteKäikudeArv = 0;
    private Integer oluliseVeaIndeks;
    private int raskusparameeter;
    private int oodatudRaskusparameeter;


    protected void suurendaValedeKäikudeArvu() {
        this.valedeKäikudeArv += 1;
    }

    protected void suurendaÕigeteKäikudeArvu() {
        this.õigeteKäikudeArv += 1;
    }

    public int getValedeKäikudeArv() {
        return valedeKäikudeArv;
    }

    public Integer getOluliseVeaIndeks() {
        return oluliseVeaIndeks;
    }


    protected void setOlulineViga(Integer oluliseVeaIndeks) {
        this.oluliseVeaIndeks = oluliseVeaIndeks;
    }

    public int getRaskusparameeter() {
        return raskusparameeter;
    }

    protected void setRaskusparameeter(int raskusparameeter) {
        this.raskusparameeter = raskusparameeter;
    }

    public int getOodatudRaskusparameeter() {
        return oodatudRaskusparameeter;
    }

    protected void setOodatudRaskusparameeter(int oodatudRaskusparameeter) {
        this.oodatudRaskusparameeter = oodatudRaskusparameeter;
    }

    private double arvutaSuhtelineRaskus() {
        double raskuseSuhtelineHinnang = 1.0 * raskusparameeter / oodatudRaskusparameeter;
        return Math.min(raskuseSuhtelineHinnang, 1);
    }

    private double arvutaÕigeteKäikudeOsakaal() {
        return 1.0 * õigeteKäikudeArv / (õigeteKäikudeArv + valedeKäikudeArv);
    }

    public double arvutaPunktid() {
        double õigeteKäikudeOsakaal = arvutaÕigeteKäikudeOsakaal();
        double raskuseSuhtelineHinnang = arvutaSuhtelineRaskus();
        if (oodatudRaskusparameeter == 0) {
            raskuseSuhtelineHinnang = 1;
            System.out.println("Oodatud raskusparameeter oli 0."); // see on kahtlane olukord ja vajab tähelepanu.
        }

        return õigeteKäikudeOsakaal * raskuseSuhtelineHinnang;
    }

    public String toString() {
        double õigeteKäikudeOsakaal = arvutaÕigeteKäikudeOsakaal();
        double raskuseSuhtelineHinnang = arvutaSuhtelineRaskus();
        String oluliseVeaInfo = oluliseVeaIndeks == null ? "puudub" : oluliseVeaIndeks + ". käigul";

        return String.format("Raskusparameeter %d/%d ehk %.2f, õigeid käike %d/%d ehk %.2f, oluline viga %s, kokku punkte %.2f.",
                raskusparameeter, oodatudRaskusparameeter, raskuseSuhtelineHinnang, õigeteKäikudeArv, õigeteKäikudeArv + valedeKäikudeArv, õigeteKäikudeOsakaal, oluliseVeaInfo, arvutaPunktid());

    }
}
