package ee.ut.labimangija.arraygrader.massiiviseis;


import java.util.Arrays;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public class MassiiviSeis {
    private final int[] massiiv;
    private Integer tööalaAlgusIndeks;
    private Integer tööalaleJärgnevIndeks;

    public MassiiviSeis(int[] massiiv, Integer tööalaAlgusIndeks, Integer tööalaleJärgnevIndeks) {
        this.massiiv = Arrays.copyOf(massiiv, massiiv.length);
        this.tööalaAlgusIndeks = tööalaAlgusIndeks;
        this.tööalaleJärgnevIndeks = tööalaleJärgnevIndeks;
    }

    public int[] getMassiiv() {
        return massiiv;
    }

    public Integer getTööalaAlgusIndeks() {
        return tööalaAlgusIndeks;
    }

    public void setTööalaAlgusIndeks(int tööalaAlgusIndeks) {
        this.tööalaAlgusIndeks = tööalaAlgusIndeks;
    }

    public Integer getTööalaleJärgnevIndeks() {
        return tööalaleJärgnevIndeks;
    }

    public void setTööalaleJärgnevIndeks(int tööalaleJärgnevIndeks) {
        this.tööalaleJärgnevIndeks = tööalaleJärgnevIndeks;
    }

    public MassiiviSeis teeKoopia() {
        return new MassiiviSeis(this.massiiv, this.tööalaAlgusIndeks, this.tööalaleJärgnevIndeks);
    }

    public boolean kasTööalaValimata() {
        return tööalaAlgusIndeks == null || tööalaleJärgnevIndeks == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < massiiv.length; i++) {
            if (!kasTööalaValimata() && (i == tööalaAlgusIndeks || i == tööalaleJärgnevIndeks)) {
                sb.append("| ");
            }
            sb.append(massiiv[i]);
            if (i != massiiv.length - 1) {
                sb.append(" ");
            }
        }
        if (getTööalaleJärgnevIndeks() != null && tööalaleJärgnevIndeks == massiiv.length) {
            sb.append("|");
        }
        sb.append("]");
        return sb.toString();
    }
}

