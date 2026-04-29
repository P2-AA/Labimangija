module ee.ut.labimangija {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens ee.ut.labimangija to javafx.fxml;
    opens ee.ut.labimangija.ui to javafx.fxml;
    opens ee.ut.labimangija.algorithmgrader.Controllers to javafx.fxml;
    opens ee.ut.labimangija.graphgrader.Kontrollerid to javafx.fxml;
    opens ee.ut.labimangija.hashgrader to javafx.fxml;
    exports ee.ut.labimangija;
}

