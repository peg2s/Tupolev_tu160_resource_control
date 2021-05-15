package controller;

import data.Aircraft;
import data.Engineer;
import data.SavedData;
import data.enums.Rank;
import io.writer.WriteFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static data.TextConstants.*;

public class FaqTabController {
    @FXML
    private Menu editEngineerButton;
    @FXML
    private Menu addEngineerButton;
    @FXML
    private Menu deleteEngineerButton;
    @FXML
    private TableView<Engineer> listOfEngineers;
    @FXML
    private TableColumn<String, String> rankColumn;
    @FXML
    private TableColumn<String, String> fullNameColumn;
    @FXML
    private ChoiceBox<String> militaryRank;
    @FXML
    private TextField fullEngineersName;
    @FXML
    private TextArea introduction;
    @FXML
    private TextArea prepairingToWork;
    @FXML
    private TextArea workInstruction;
    @FXML
    private TextArea saveAndLoadInstruction;
    @FXML
    private TextArea feedbackText;
    @FXML
    private TableView<Aircraft> aircraftsList;
    @FXML
    private TextField sideNumberField;
    @FXML
    private TextField regNumberField;
    @FXML
    private TextField aircraftName;
    @FXML
    private ChoiceBox<String> selectEngineerBox;
    @FXML
    private Button addAircraftButton;
    @FXML
    private Button deleteAircraftButton;
    @FXML
    private Button editAircraftButton;
    @FXML
    private Tab aircraftComponentsList;
    @FXML
    private TextField componentNumberField;
    @FXML
    private ChoiceBox<?> selectComponentTypeBox;
    @FXML
    private TextField currentOperationTimeField;
    @FXML
    private TextField lastOperationTimeField;
    @FXML
    private TextField allowedMaxOperatingTime;
    @FXML
    private TextField remainingOperationgTimeField;
    @FXML
    private Button addComponentButton;
    @FXML
    private Button deleteComponentButton;
    @FXML
    private Button editComponentButton;
    @FXML
    private TableColumn<String, String> sideNumberColumn;
    @FXML
    private TableColumn<String, String> regNumberColumn;
    @FXML
    private TableColumn<String, String> engineerOfAircraftName;
    @FXML
    private TableColumn<String, String> aircraftNameColumn;
    @FXML
    void initialize() {
//        listOfEngineers.setOnMouseClicked(e -> fillSelectedEngineerInfo());
//        aircraftsList.setOnMouseClicked(e -> fillSelectedAircraftInfo());
//        fillFaqText();
//        updateEngineersList();
//        updateAircraftsList();
//        fullEngineersName.setText("");
//        militaryRank.setItems(prepareMilitaryRanks());
//        prepareEngineersListOnAircraftTab();
//        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
//        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
//        sideNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sideNumber"));
//        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
//        aircraftNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//        engineerOfAircraftName.setCellValueFactory(new PropertyValueFactory<>("engineerName"));

    }


    ObservableList<String> prepareMilitaryRanks() {
        militaryRank.setTooltip(new Tooltip("Выбери звание инженера!"));
        ArrayList<String> list = EnumSet.allOf(Rank.class)
                .stream()
                .map(Rank::getDescription)
                .collect(Collectors.toCollection(ArrayList::new));
        return FXCollections.observableList(list);
    }

    void prepareEngineersListOnAircraftTab() {
        if (listOfEngineers.getItems().size() > 0) {
            selectEngineerBox.setItems(FXCollections.observableList(
                    listOfEngineers.getItems()
                            .stream()
                            .map(Engineer::toString).
                            collect(Collectors.toList())));
        }
    }

    void updateEngineersList() {
        if (SavedData.engineers.size() == 0) {
            listOfEngineers.setPlaceholder(new Label("Не введены данные об инженерах.\n" +
                    "Добавьте хотя бы одного человека."));
        }
        listOfEngineers.getItems().clear();
        for (Engineer engineer : SavedData.engineers) {
            listOfEngineers.getItems().addAll(engineer);
        }
    }



    void fillSelectedAircraftInfo() {
        Aircraft aircraft;
        if (aircraftsList.getSelectionModel().getSelectedItem() != null) {
            aircraft = aircraftsList.getSelectionModel().getSelectedItem();
            aircraftName.setText(aircraft.getName());
            sideNumberField.setText(aircraft.getSideNumber());
            regNumberField.setText(aircraft.getRegNumber());
            selectEngineerBox.setValue(aircraft.getEngineer().toString());
        }
    }

    @FXML
    void fillFaqText() {
        introduction.setText(INTRODUCTION_FAQ);
        prepairingToWork.setText(PREPAIRING_TO_WORK_FAQ);
        workInstruction.setText(WORK_INSTRUCTIONS_FAQ);
        saveAndLoadInstruction.setText(SAVE_AND_LOAD_FAQ);
        feedbackText.setText(CONTACTS_AND_FEEDBACK_FAQ);
    }



    @FXML
    void deleteEngineer(ActionEvent event) {
        SavedData.engineers.remove(listOfEngineers.getSelectionModel().getSelectedItem());
        SavedData.saveCurrentStateData();
        listOfEngineers.getItems().remove(listOfEngineers.getSelectionModel().getSelectedItem());
        listOfEngineers.refresh();
    }





    @FXML
    void deleteAircraft(ActionEvent event) {
        SavedData.aircraft.remove(aircraftsList.getSelectionModel().getSelectedItem());
SavedData.saveCurrentStateData();        aircraftsList.getItems().remove(aircraftsList.getSelectionModel().getSelectedItem());
        aircraftsList.refresh();
    }


    void updateAircraftsList() {
        if (SavedData.aircraft.size() == 0) {
            aircraftsList.setPlaceholder(new Label("Нет ни одной записи.\n" +
                    "Добавьте хотя бы один самолёт."));
        }
        aircraftsList.getItems().clear();
        for (Aircraft aircraft : SavedData.aircraft) {
            aircraftsList.getItems().addAll(aircraft);
        }
        aircraftsList.refresh();
    }


}

