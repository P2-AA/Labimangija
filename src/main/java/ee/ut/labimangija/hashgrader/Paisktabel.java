package ee.ut.labimangija.hashgrader;

import java.util.ArrayList;

public class Paisktabel<T> {

    private final int kompesamm;
    private ArrayList<ArrayList<T>> tabel;

    public Paisktabel(int kompesamm) {
        this.kompesamm = kompesamm;
        this.tabel = new ArrayList<>();
    }

    public Paisktabel(int kompesamm, int len) {
        this.kompesamm = kompesamm;
        this.tabel = new ArrayList<>();
        looPaisktabel(len);
    }

    public void looPaisktabel(int len) {
        for (int i = 0; i < len; i++) {
            this.tabel.add(new ArrayList<>());
        }
    }

    public void hävitaPaisktabel() {
        this.tabel = new ArrayList<>();
    }

    public boolean sisesta(int r, int k, T elem) {
        tabel.get(r).add(k, elem);
        return true;
    }

    public boolean eemalda(int r, int k) {
        ArrayList<T> ahel = tabel.get(r);
        if (ahel.size() > k) {
            ahel.remove(k);
            return true;
        }
        return false;
    }

    public int leiaVabaKoht(int räsi) {
        int indeks = räsi;
        while (tabel.get(indeks).size() > 0) {
            indeks += kompesamm;
            if (indeks >= tabel.size()) {
                indeks -= tabel.size();
            }
            if (indeks == räsi) {
                return -1;
            }
        }
        return indeks;
    }

    public int leiaAsukoht(T element, int räsi) {
        int indeks = räsi;
        while (!tabel.get(indeks).contains(element)) {
            indeks += kompesamm;
            if (indeks >= tabel.size()) {
                indeks -= tabel.size();
            }
            if (indeks == räsi || tabel.get(indeks) == null) {
                return -1;
            }
        }
        return indeks;
    }

    public int size() {
        return tabel.size();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < tabel.size(); i++) {
            str.append(i).append(":");
            if (!tabel.get(i).isEmpty()) {
                str.append(vormindaAhel(tabel.get(i)));
            }
            str.append("\n");
        }
        return str.toString();
    }

    private String vormindaAhel(ArrayList<T> ahel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ahel.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(ahel.get(i));
        }
        return sb.toString();
    }

    public T get(int r, int k) {
        ArrayList<T> jada = tabel.get(r);
        if (jada.size() == 0) {
            return null;
        }
        return jada.get(k);
    }

    public ArrayList<T> get(int r) {
        return tabel.get(r);
    }
}

