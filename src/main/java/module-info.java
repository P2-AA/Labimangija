module org.example.labimangija {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens org.example.labimangija to javafx.fxml;
    exports org.example.labimangija;
}