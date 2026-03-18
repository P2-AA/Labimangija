package org.example.labimangija.hashgrader.samm;

import java.util.Objects;
import org.example.labimangija.hashgrader.Läbimäng;

public class PaisktabeliLoomiseSamm implements Samm {
    private final float minElement;
    private final float maxElement;
    private final int paisktabeliPikkus;

    public PaisktabeliLoomiseSamm(int paisktabeliPikkus) {
        this(0, 0, paisktabeliPikkus);
    }

    public PaisktabeliLoomiseSamm(float minElement, float maxElement, int paisktabeliPikkus) {
        this.minElement = minElement;
        this.maxElement = maxElement;
        this.paisktabeliPikkus = paisktabeliPikkus;
    }

    @Override
    public boolean astu(Läbimäng läbimäng) {
        läbimäng.setPaisktabeliParameetrid(minElement, maxElement, paisktabeliPikkus);
        läbimäng.getPaisktabel().looPaisktabel(paisktabeliPikkus);
        return true;
    }

    @Override
    public boolean tagasi(Läbimäng läbimäng) {
        läbimäng.getPaisktabel().hävitaPaisktabel();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaisktabeliLoomiseSamm that = (PaisktabeliLoomiseSamm) o;
        return paisktabeliPikkus == that.paisktabeliPikkus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paisktabeliPikkus);
    }

    @Override
    public String toString() {
        return "Uus paisktabel: minElement=" + minElement + "; maxElement=" + maxElement + "; pikkus=" + paisktabeliPikkus;
    }
}
