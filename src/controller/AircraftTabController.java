package controller;

import data.Aircraft;
import data.Engineer;
import data.SavedData;
import data.TextConstants;
import gui.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import utils.TextUtils;

import java.util.ArrayList;

import static utils.ServiceUtils.checkAircraftDuplicate;
import static utils.ServiceUtils.showWarning;
import static utils.TextUtils.*;

@Slf4j
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
    @FXML
    private PersonalAircraftPageController personalAircraftPageController;
    private Aircraft selectedAircraft;

    @FXML
    void initialize() {
        aircraftsList.setPlaceholder(new Label(TextConstants.NO_AIRCRAFT_RECORDS));
        setRestrictionsToInputFields();
        aircraftName.setPromptText("Имя самолёта");
        regNumberField.setPromptText("RF-*****");
        sideNumberField.setPromptText("б/н");
        reloadInfoOnTab();
        aircraftsList.setOnMouseClicked(this::handleClickOnAircraftList);
        sideNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sideNumber"));
        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        aircraftNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        engineerOfAircraftName.setCellValueFactory(new PropertyValueFactory<>("engineer"));
    }

    void updateEngineersListOnAircraftTab() {
        selectEngineerBox.getItems().clear();
        selectEngineerBox.getItems().addAll(FXCollections.observableList(SavedData.engineers));
    }

    void fillSelectedAircraftInfo() {
        if (aircraftsList.getSelectionModel().getSelectedItem() != null) {
            selectedAircraft = aircraftsList.getSelectionModel().getSelectedItem();
            aircraftName.setText(selectedAircraft.getName());
            sideNumberField.setText(selectedAircraft.getSideNumber());
            regNumberField.setText(selectedAircraft.getRegNumber());
            selectEngineerBox.getSelectionModel()
                    .select(SavedData.getEngineerFromList(
                            selectedAircraft.getEngineer()));
        }
    }

    @FXML
    void addAircraft() {
        if (checkInputFields()) {
            Aircraft aircraft = Aircraft.builder()
                    .name(aircraftName.getText())
                    .regNumber(regNumberField.getText())
                    .sideNumber(sideNumberField.getText())
                    .engineer(selectEngineerBox.getSelectionModel().getSelectedItem().toString())
                    .components(new ArrayList<>())
                    .build();
            if (!checkAircraftDuplicate(aircraft)) {
                selectEngineerBox.getSelectionModel().getSelectedItem().getAttachedAircrafts().add(aircraft);
                SavedData.engineers = selectEngineerBox.getItems();
                aircraftsList.getItems().addAll(aircraft);
                SavedData.aircraft.add(aircraft);
                SavedData.saveCurrentStateData();
                log.info("добавлено ВС: \n б.н.: {}\n рег.н.: {} \n наименование: {}\n ИАК: {}",
                        aircraft.getSideNumber(), aircraft.getRegNumber(),
                        aircraft.getName(), aircraft.getEngineer());
            }
        }
    }

    @FXML
    void deleteAircraft() {
        Aircraft aircraftToDelete = aircraftsList.getSelectionModel().getSelectedItem();
        SavedData.components.stream().filter(c -> c.getAttachedToAircraft().equals(aircraftToDelete.toString())).forEach(
                component -> {
                    component.setUnmounted(true);
                    component.setAttachedToAircraft(TextConstants.UNATTACHED_FROM_AIRCRAFT);
                }
        );
        SavedData.engineers.stream().filter(engineer -> engineer.getAttachedAircrafts().contains(aircraftToDelete)).forEach(
                engineer ->
                        engineer.getAttachedAircrafts().remove(aircraftToDelete));
        SavedData.aircraft.remove(aircraftToDelete);
        log.info("Удалено ВС: {}", aircraftToDelete);
        SavedData.saveCurrentStateData();
        aircraftsList.getItems().remove(aircraftToDelete);
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
                        selectedAircraft.setRegNumber("RF-" + regNumberField.getText());
                        selectedAircraft.setEngineer(selectEngineerBox.getSelectionModel().getSelectedItem().toString());
                        selectedAircraft.setName(aircraftName.getText());
                    });
        }
        SavedData.saveCurrentStateData();
        updateAircraftsList();
        log.info("Редактирование ВС: {}", selectedAircraft);
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
        log.info("Переприкрепление ИАК на самолете: {} на: {}",
                selectedAircraft, selectEngineerBox.getSelectionModel().getSelectedItem());
        SavedData.saveCurrentStateData();
    }

    void updateAircraftsList() {
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
        dialogStage.setResizable(false);
        dialogStage.getIcons().add(new Image("/images/logo.jpg"));

        PersonalAircraftPageController controller = loader.getController();
        controller.setAircraftTabController(this);
        controller.setAircraft(aircraftsList.getSelectionModel().getSelectedItem());
        dialogStage.show();
        SavedData.readDataFromSave();
        personalAircraftPageController = controller;
    }

    private boolean checkInputFields() {
        boolean checkOk =  checkInputText(regNumberField, sideNumberField, aircraftName)
                && selectEngineerBox.getSelectionModel().getSelectedItem() != null;
        if (!checkOk) {
            showWarning(TextConstants.EMPTY_FIELD_WARNING);
            return false;
        } else return TextUtils.checkAircraftRegNumber(regNumberField.getText());
    }

    private void setRestrictionsToInputFields() {
        sideNumberField.setTextFormatter(createFormatterForOnlyDigits(2));
        aircraftName.setTextFormatter(createFormatterForOnlyText("^(?i)[А-Я,а-я? ]{0,40}$"));
        regNumberField.setTextFormatter(createFormatterForOnlyText("^[0-9]{0,5}"));
    }
}

