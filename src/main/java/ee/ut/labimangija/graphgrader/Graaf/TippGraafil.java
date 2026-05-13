package ee.ut.labimangija.graphgrader.Graaf;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
// Klassi implementatsioon põhineb Peamiselt Erik Presnovi loodud lahendusel.
// Eeskujuks kasutatud töö: "Graafialgoritmide läbimängija ja hindaja", kättesaadav aadressil:
// https://thesis.cs.ut.ee/4d0c5318-13c9-4260-92e1-9d2b1c815dc7
public class TippGraafil extends Circle {

    public Tipp tipp;

    public TippGraafil(double centerX, double centerY, double radius, Tipp tipp) {
        super(centerX, centerY, radius);
        this.tipp = tipp;
        setStroke(Color.BLACK);
        setStrokeWidth(1.0);
    }
}


