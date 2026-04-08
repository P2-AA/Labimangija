package org.example.labimangija;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.labimangija.arraygrader.kasutajaliides.ArrayGraderEngine;

public class ArrayGraderController {
    private final ArrayGraderEngine engine = new ArrayGraderEngine();

    @FXML
    private TabPane algorithmTabPane;

    @FXML
    private Tab mullimeetodTab;

    @FXML
    private Tab pistemeetodTab;

    @FXML
    private Tab valikumeetodTab;

    @FXML
    private Tab valikuKiirmeetodTab;

    @FXML
    private ChoiceBox<ArrayGraderEngine.Reziim> modeChoiceBox;

    @FXML
    private TextArea terminalArea;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField startIndexField;

    @FXML
    private TextField endIndexField;

    @FXML
    private TextField firstIndexField;

    @FXML
    private TextField secondIndexField;

    @FXML
    private TextField partitionField;

    @FXML
    private TextField commandField;

    @FXML
    private Button pisteButton;

    @FXML
    private Button swapButton;

    @FXML
    private Button partitionButton;

    @FXML
    private void initialize() {
        modeChoiceBox.setItems(FXCollections.observableArrayList(ArrayGraderEngine.Reziim.values()));
        modeChoiceBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(ArrayGraderEngine.Reziim object) {
                return object == null ? "" : object.getPealkiri();
            }

            @Override
            public ArrayGraderEngine.Reziim fromString(String string) {
                return null;
            }
        });
        modeChoiceBox.getSelectionModel().select(ArrayGraderEngine.Reziim.HARJUTAMINE);
        algorithmTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> uuendaVaadet());
        uuendaVaadet();
    }

    @FXML
    private void handleStart() {
        engine.alusta(getSelectedAlgorithm(), modeChoiceBox.getValue());
        puhastaSisendiValjad();
        uuendaVaadet();
    }

    @FXML
    private void handleSetWorkArea() {
        executeCommand("tööala " + loe(startIndexField, "Sisesta tööala algusindeks.")
                + " " + loe(endIndexField, "Sisesta tööala lõpuindeks."));
    }

    @FXML
    private void handlePiste() {
        executeCommand("piste " + loe(firstIndexField, "Sisesta piste algusindeks.")
                + " " + loe(secondIndexField, "Sisesta piste lõpuindeks."));
    }

    @FXML
    private void handleSwap() {
        executeCommand("vaheta " + loe(firstIndexField, "Sisesta esimene vahetatav indeks.")
                + " " + loe(secondIndexField, "Sisesta teine vahetatav indeks."));
    }

    @FXML
    private void handlePartition() {
        String partition = loe(partitionField, "Sisesta jaotuse uus massiiv.");
        executeCommand("jaota " + partition);
    }

    @FXML
    private void handleUndo() {
        executeCommand("tagasi");
    }

    @FXML
    private void handleFinish() {
        executeCommand("lõpeta");
    }

    @FXML
    private void handleSubmitCommand() {
        executeCommand(loe(commandField, "Sisesta käsk."));
    }

    private void executeCommand(String command) {
        try {
            engine.executeCommand(command);
            commandField.clear();
        } catch (IllegalArgumentException e) {
            statusLabel.setText(e.getMessage());
        }
        uuendaVaadet();
    }

    private void uuendaVaadet() {
        terminalArea.setText(engine.render());
        statusLabel.setText(engine.getViimaneTeade());

        boolean editable = engine.onMuudetav();
        startIndexField.setDisable(!editable);
        endIndexField.setDisable(!editable);
        firstIndexField.setDisable(!editable);
        secondIndexField.setDisable(!editable);
        partitionField.setDisable(!editable || !engine.supportsJaota());
        commandField.setDisable(!editable);

        pisteButton.setDisable(!editable || !engine.supportsPiste());
        swapButton.setDisable(!editable || !engine.supportsVaheta());
        partitionButton.setDisable(!editable || !engine.supportsJaota());
    }

    private void puhastaSisendiValjad() {
        startIndexField.clear();
        endIndexField.clear();
        firstIndexField.clear();
        secondIndexField.clear();
        partitionField.clear();
        commandField.clear();
    }

    private String loe(TextField field, String veateade) {
        String vaartus = field.getText() == null ? "" : field.getText().trim();
        if (vaartus.isEmpty()) {
            throw new IllegalArgumentException(veateade);
        }
        return vaartus;
    }

    private ArrayGraderEngine.Algoritm getSelectedAlgorithm() {
        Tab selectedTab = algorithmTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == pistemeetodTab) {
            return ArrayGraderEngine.Algoritm.PISTEMEETOD;
        }
        if (selectedTab == valikumeetodTab) {
            return ArrayGraderEngine.Algoritm.VALIKUMEETOD;
        }
        if (selectedTab == valikuKiirmeetodTab) {
            return ArrayGraderEngine.Algoritm.VALIKU_KIIRMEETOD;
        }
        return ArrayGraderEngine.Algoritm.MULLIMEETOD;
    }
}
