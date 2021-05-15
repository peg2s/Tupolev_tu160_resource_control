package controller;

import data.Aircraft;
import data.Component;
import data.MKU;
import data.SavedData;
import data.enums.ComponentType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;

import static utils.TextUtils.checkInputText;
import static utils.TextUtils.createFormatter;

public class PersonalComponentPageController {

    @FXML
    private ChoiceBox<Aircraft> componentAttachedTo;
    @FXML
    private TextField componentNumber;
    @FXML
    private ChoiceBox<ComponentType> componentType;
    @FXML
    private TextField hoursOperTime;
    @FXML
    private TextField minutesOperTime;
    @FXML
    private TextField takeOffCount;
    @FXML
    private TextField mainChannelFireCount;
    @FXML
    private TextField reserveChannelFireCount;
    @FXML
    private TextField additionalOperTimeHours;
    @FXML
    private TextField additionalOperTimeMinutes;
    @FXML
    private TextField additionalTakeOffCount;
    @FXML
    private TextField additionalMainChannelFireCount;
    @FXML
    private TextField additionalReserveChannelFireCount;
    @FXML
    private Button addLastOperationsButton;
    @FXML
    private Button createButton;
    @FXML
    private TextField rotationsCount;
    @FXML
    private TextField additionalRotationsCount;
    @Getter
    private Component createdComponent;
    private AircraftComponentsTabController controller;
    private boolean isEditMode;
    private int componentIdInSavedData;

    @FXML
    void initialize() {
        // установим ограничение на ввод данных: только цифры
        hoursOperTime.setTextFormatter(createFormatter());
        minutesOperTime.setTextFormatter(createFormatter());
        componentNumber.setTextFormatter(createFormatter());
        rotationsCount.setTextFormatter(createFormatter());
        mainChannelFireCount.setTextFormatter(createFormatter());
        reserveChannelFireCount.setTextFormatter(createFormatter());
        takeOffCount.setTextFormatter(createFormatter());

        // подгрузим в ОЗУ последние данные из сохранений
        SavedData.readDataFromSave();

        // заполним базовые значения полей
        componentType.getItems().addAll(FXCollections.observableList(new ArrayList<>(EnumSet.allOf(ComponentType.class))));
        componentAttachedTo.getItems().addAll(SavedData.aircraft);
        createButton.setOnAction(e -> {
            handleClickCreateButton();
            controller.updateComponentsList();
        });
    }

    public void prepareComponentToEdit(Component component) {
        isEditMode = true;
        componentIdInSavedData = SavedData.getComponentId(component);
        this.createdComponent = component;
        fillComponentInfo();
    }

    void createComponent() {
        if (componentType.getSelectionModel().getSelectedItem() == ComponentType.L029) {
            // todo: отрисовка иной формы ввода для данного типа компонент
        } else {
            if (checkInputIsNotBlank()) {
                int flightTime = Integer.parseInt(hoursOperTime.getText()) * 60 + Integer.parseInt(minutesOperTime.getText());
                createdComponent = new MKU(Integer.parseInt(componentNumber.getText()),
                        Integer.parseInt(takeOffCount.getText()),
                        Integer.parseInt(mainChannelFireCount.getText()),
                        Integer.parseInt(reserveChannelFireCount.getText()),
                        flightTime,
                        Integer.parseInt(rotationsCount.getText()),
                        componentAttachedTo.getSelectionModel().getSelectedItem().toString());
                if (isEditMode) {
                    SavedData.components.set(componentIdInSavedData, createdComponent);
                } else {
                    SavedData.components.add(createdComponent);
                }
                SavedData.saveCurrentStateData();
            }
        }
    }

    @FXML
    public void handleClickCreateButton() {
        createComponent();
        if (createdComponent != null) {
            controller.setSelectedComponent(createdComponent);
            ((Stage) createButton.getScene().getWindow()).close();
        }
    }

    void setParentController(AircraftComponentsTabController controller) {
        this.controller = controller;
    }

    boolean checkInputIsNotBlank() {
        boolean isNotBlank = checkInputText(componentNumber.getText(),
                hoursOperTime.getText(),
                minutesOperTime.getText(),
                takeOffCount.getText(),
                mainChannelFireCount.getText(),
                reserveChannelFireCount.getText(),
                rotationsCount.getText())
                && componentType.getSelectionModel().getSelectedItem() != null
                && componentAttachedTo.getSelectionModel().getSelectedItem() != null;
        if (!isNotBlank) {
            showWarning("Проверьте заполнение полей! \n" +
                    "Все поля должны быть заполнены!");
        }
        return isNotBlank;
    }

    void fillComponentInfo() {
        if (createdComponent instanceof MKU) {
            MKU mku = (MKU) createdComponent;
            hoursOperTime.setText(String.valueOf(mku.getFlightOperatingTime() / 60));
            minutesOperTime.setText(String.valueOf(mku.getFlightOperatingTime() % 60));
            reserveChannelFireCount.setText(String.valueOf(mku.getStartsOnReserveChannel()));
            mainChannelFireCount.setText(String.valueOf(mku.getStartsOnMainChannel()));
            rotationsCount.setText(String.valueOf(mku.getRotationsCount()));
            takeOffCount.setText(String.valueOf(mku.getCountOfLandings()));
            componentAttachedTo.setValue(SavedData.aircraft.stream()
                    .filter(a -> a.toString()
                            .equals(((MKU) createdComponent)
                                    .getAttachedToAircraft()))
                    .findAny().get());
            componentNumber.setText(String.valueOf(((MKU) createdComponent).getNumber()));
            componentType.setValue(((MKU) createdComponent).getType());
        }
    }

    void showWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
