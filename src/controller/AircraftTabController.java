package controller;

import data.Aircraft;
import data.Engineer;
import data.SavedData;
import gui.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class AircraftTabController {

    @FXML
    @Getter
    @Setter
    MainController mainController;
    @FXML
    @Getter
    @Setter
    private TableView<Aircraft> aircraftsList;
    @FXML
    private TextField sideNumberField;
    @FXML
    private TextField regNumberField;
    @FXML
    private TextField aircraftName;
    @FXML
    private ChoiceBox<Engineer> selectEngineerBox;
    @FXML
    private TableColumn<String, String> sideNumberColumn;
    @FXML
    private TableColumn<String, String> regNumberColumn;
    @FXML
    private TableColumn<Engineer, Engineer> engineerOfAircraftName;
    @FXML
    private TableColumn<String, String> aircraftNameColumn;
    @Getter
    private PersonalAircraftPageController personalAircraftPageController;
    private Aircraft selectedAircraft;

    @FXML
    void initialize() {
        reloadInfoOnTab();
        aircraftsList.setOnMouseClicked(this::handleClickOnAircraftList);
        sideNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sideNumber"));
        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        aircraftNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        engineerOfAircraftName.setCellValueFactory(new PropertyValueFactory<>("engineer"));
    }

    void updateEngineersListOnAircraftTab() {
        if (SavedData.engineers.size() > 0) {
            selectEngineerBox.getItems().clear();
            selectEngineerBox.getItems().addAll(FXCollections.observableList(SavedData.engineers));
        }
    }

    void fillSelectedAircraftInfo() {
        if (aircraftsList.getSelectionModel().getSelectedItem() != null) {
            selectedAircraft = aircraftsList.getSelectionModel().getSelectedItem();
            aircraftName.setText(selectedAircraft.getName());
            sideNumberField.setText(selectedAircraft.getSideNumber());
            regNumberField.setText(selectedAircraft.getRegNumber());
        }
    }

    @FXML
    void addAircraft() {
        if (StringUtils.isNotBlank(sideNumberField.getCharacters())
                && StringUtils.isNotBlank(regNumberField.getCharacters())
                && StringUtils.isNotBlank(aircraftName.getCharacters())
                && selectEngineerBox.getSelectionModel().getSelectedItem() != null) {

            Aircraft aircraft = Aircraft.builder()
                    .name(aircraftName.getText())
                    .regNumber(regNumberField.getText())
                    .sideNumber(sideNumberField.getText())
                    .engineer(selectEngineerBox.getSelectionModel().getSelectedItem().toString())
                    .components(new ArrayList<>())
                    .build();
            selectEngineerBox.getSelectionModel().getSelectedItem().getAttachedAircrafts().add(aircraft);
            SavedData.engineers = selectEngineerBox.getItems();
            aircraftsList.getItems().addAll(aircraft);
            SavedData.aircraft.add(aircraft);
            SavedData.saveCurrentStateData();
        }
    }

    @FXML
    void deleteAircraft() {
        SavedData.aircraft.remove(aircraftsList.getSelectionModel().getSelectedItem());
        SavedData.saveCurrentStateData();
        aircraftsList.getItems().remove(aircraftsList.getSelectionModel().getSelectedItem());
        aircraftsList.refresh();
    }

    @FXML
    void editAircraft() {
        if (StringUtils.isNotBlank(sideNumberField.getCharacters())
                && StringUtils.isNotBlank(regNumberField.getCharacters())
                && StringUtils.isNotBlank(aircraftName.getCharacters())
                && selectEngineerBox.getSelectionModel().getSelectedItem() != null) {

            reattachAircraft(selectedAircraft);
            SavedData.aircraft.stream().filter(a -> a.equals(selectedAircraft))
                    .forEach(a -> {
                        selectedAircraft.setSideNumber(sideNumberField.getText());
                        selectedAircraft.setRegNumber(regNumberField.getText());
                        selectedAircraft.setEngineer(selectEngineerBox.getSelectionModel().getSelectedItem().toString());
                        selectedAircraft.setName(aircraftName.getText());
                    });
        }
        SavedData.saveCurrentStateData();
        updateAircraftsList();
    }

    void reloadInfoOnTab() {
        updateAircraftsList();
        updateEngineersListOnAircraftTab();
        aircraftsList.refresh();
    }

    void reattachAircraft(Aircraft aircraft) {
        SavedData.engineers
                .stream()
                .filter(e -> e.toString().equals(aircraft.getEngineer()))
                .forEach(engineer -> engineer.getAttachedAircrafts().remove(aircraft));
        selectEngineerBox.getSelectionModel().getSelectedItem().getAttachedAircrafts().add(selectedAircraft);
        SavedData.saveCurrentStateData();
    }

    void updateAircraftsList() {
        if (SavedData.aircraft.size() == 0) {
            aircraftsList.setPlaceholder(new Label("Нет ни одной записи.\n" +
                    "Добавьте хотя бы один самолёт."));
        }
        aircraftsList.getItems().clear();
        aircraftsList.getItems().addAll(SavedData.aircraft);
    }

    void handleClickOnAircraftList(MouseEvent event) {
        if (event.getClickCount() == 2) {
            openPersonalAircraftPage();
        }
        fillSelectedAircraftInfo();
    }

    @FXML
    @SneakyThrows
    void openPersonalAircraftPage() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/personalAircraftPage.fxml"));
        AnchorPane page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        PersonalAircraftPageController controller = loader.getController();
        controller.setAircraftTabController(this);
        controller.setAircraft(aircraftsList.getSelectionModel().getSelectedItem());
        dialogStage.show();
        SavedData.readDataFromSave();
        personalAircraftPageController = controller;
    }
}

