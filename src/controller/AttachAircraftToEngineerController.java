package controller;

import data.Aircraft;
import data.SavedData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.util.Optional;

public class AttachAircraftToEngineerController {

    @FXML
    private ListView<Aircraft> listOfAircaftToAttach;

    @FXML
    private Button attachButton;

    @SneakyThrows
    @FXML
    void initialize() {
        listOfAircaftToAttach.getItems().addAll(SavedData.aircraft);
    }

    public Optional<Aircraft> handleClickAttachButton() {
        ((Stage) attachButton.getScene().getWindow()).close();
        return Optional.ofNullable(listOfAircaftToAttach.getSelectionModel().getSelectedItem());
    }

}
