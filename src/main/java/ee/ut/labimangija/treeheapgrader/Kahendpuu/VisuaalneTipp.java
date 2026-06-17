package ee.ut.labimangija.treeheapgrader.Kahendpuu;

import javafx.scene.shape.Circle;

// Klassi implementatsioon põhineb peamiselt Markus Michelise loodud lahendusel.
// Eeskujuks kasutatud töö: "kahendpuu- ja kuhjaalgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/e07a9cf2-900d-4db8-9d05-5c24d48e424c

public class VisuaalneTipp extends Circle {
    public Tipp tipp;
    public int väärtus;

    public VisuaalneTipp(double centerX, double centerY, double raadius, Tipp tipp) {
        super(centerX, centerY, raadius);
        this.tipp = tipp;
    }
}

