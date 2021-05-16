package controller;

import data.Aircraft;
import data.Component;
import data.MPU_MKU;
import data.SavedData;
import data.enums.ComponentType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static utils.ServiceUtils.showWarning;
import static utils.TextUtils.checkInputText;
import static utils.TextUtils.createFormatterForOnlyDigits;

public class PersonalComponentPageController {

    @FXML
    private ChoiceBox<String> componentAttachedTo;
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
    @FXML
    private CheckBox isUnmounted;
    @FXML
    @Setter
    private MainController mainController;
    private boolean isUnmountedFromAircraft;
    @Getter
    private Component createdComponent;
    private Component selectedComponent;
    private AircraftComponentsTabController controller;
    private boolean isEditMode;
    private int componentIdInSavedData;

    @FXML
    void initialize() {
        isUnmounted.setOnAction(e -> handleUnmountCheckboxSelected());
        // установим ограничение на ввод данных: только цифры
        hoursOperTime.setTextFormatter(createFormatterForOnlyDigits(3));
        minutesOperTime.setTextFormatter(createFormatterForOnlyDigits(2));
        componentNumber.setTextFormatter(createFormatterForOnlyDigits(30));
        rotationsCount.setTextFormatter(createFormatterForOnlyDigits(2));
        mainChannelFireCount.setTextFormatter(createFormatterForOnlyDigits(3));
        reserveChannelFireCount.setTextFormatter(createFormatterForOnlyDigits(3));
        takeOffCount.setTextFormatter(createFormatterForOnlyDigits(4));

        // подгрузим в ОЗУ последние данные из сохранений
        SavedData.readDataFromSave();

        // заполним базовые значения полей
        componentType.getItems().addAll(FXCollections.observableList(new ArrayList<>(EnumSet.allOf(ComponentType.class))));
        componentAttachedTo.getItems().addAll(SavedData.aircraft.stream().map(Aircraft::toString).collect(Collectors.toList()));
        createButton.setOnAction(e -> {
            handleClickCreateButton();
            controller.updateComponentsList();
        });
        addLastOperationsButton.setOnAction(e -> addLastFlightInfo());
    }

    public void prepareComponentToEdit(Component component) {
        isEditMode = true;
        componentIdInSavedData = SavedData.getComponentId(component);
        this.createdComponent = component;
        this.selectedComponent = component;
        fillComponentInfo();
    }

    void createComponent() {
        if (componentType.getSelectionModel().getSelectedItem() == ComponentType.L029) {
            // todo: отрисовка иной формы ввода для данного типа компонент
        } else {
            if (checkInputIsNotBlank()) {
                createdComponent = createMPU_MKU(componentType.getValue());
                if (isEditMode) {
                    MPU_MKU componentInSave = (MPU_MKU) SavedData.components.get(componentIdInSavedData);
                    if (!componentInSave.equals(createdComponent.getAttachedToAircraft())) {
                        SavedData.aircraft.stream().filter(a -> a.toString().equals((componentInSave).getAttachedToAircraft())).forEach(a ->
                                a.getComponents().remove(selectedComponent.toString())
                        );
                        SavedData.aircraft.stream().filter(a -> a.toString().equals(createdComponent.getAttachedToAircraft())).forEach(a ->
                                a.getComponents().add(createdComponent.toString())
                        );
                    }
                    SavedData.components.set(componentIdInSavedData, createdComponent);
                } else {
                    SavedData.getAircraft(componentAttachedTo.getValue()).getComponents().add(createdComponent.toString());
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
        }
        if (checkInputIsNotBlank()) {
            ((Stage) createButton.getScene().getWindow()).close();
            mainController.getAircraftTabController().getPersonalAircraftPageController().updateComponentsList();
        } else {
            showWarning("Проверьте заполнение полей! \n" +
                    "Все поля должны быть заполнены!");
        }
    }

    void setParentController(AircraftComponentsTabController controller) {
        this.controller = controller;
    }

    boolean checkInputIsNotBlank() {
        return checkInputText(componentNumber.getText(),
                hoursOperTime.getText(),
                minutesOperTime.getText(),
                takeOffCount.getText(),
                mainChannelFireCount.getText(),
                reserveChannelFireCount.getText(),
                rotationsCount.getText())
                && componentType.getSelectionModel().getSelectedItem() != null
                && (componentAttachedTo.getSelectionModel().getSelectedItem() != null || isUnmountedFromAircraft);

    }

    void fillComponentInfo() {
        if (createdComponent instanceof MPU_MKU) {
            MPU_MKU mku = (MPU_MKU) createdComponent;
            hoursOperTime.setText(String.valueOf(mku.getFlightOperatingTime() / 60));
            minutesOperTime.setText(String.valueOf(mku.getFlightOperatingTime() % 60));
            reserveChannelFireCount.setText(String.valueOf(mku.getStartsOnReserveChannel()));
            mainChannelFireCount.setText(String.valueOf(mku.getStartsOnMainChannel()));
            rotationsCount.setText(String.valueOf(mku.getRotationsCount()));
            takeOffCount.setText(String.valueOf(mku.getCountOfLandings()));
            componentNumber.setText(String.valueOf(createdComponent.getNumber()));
            componentType.setValue(createdComponent.getType());
            isUnmounted.setSelected(createdComponent.isUnmounted());
            handleUnmountCheckboxSelected();
            if (!createdComponent.isUnmounted()) {
                componentAttachedTo.setValue(SavedData.aircraft.stream()
                        .filter(a -> a.toString()
                                .equals(createdComponent
                                        .getAttachedToAircraft()))
                        .findAny().get().toString());
            }
        }
    }

    public void setAdditionalFieldsDisabled(boolean isDisabled) {
        additionalMainChannelFireCount.setDisable(isDisabled);
        additionalOperTimeHours.setDisable(isDisabled);
        additionalOperTimeMinutes.setDisable(isDisabled);
        additionalMainChannelFireCount.setDisable(isDisabled);
        additionalRotationsCount.setDisable(isDisabled);
        additionalTakeOffCount.setDisable(isDisabled);
        additionalReserveChannelFireCount.setDisable(isDisabled);
        addLastOperationsButton.setDisable(isDisabled);
    }

    private void addLastFlightInfo() {
        if (createdComponent instanceof MPU_MKU) {
            int id = SavedData.getComponentId(createdComponent);
            if (checkInputText(additionalOperTimeHours.getText(),
                    additionalOperTimeMinutes.getText(),
                    additionalTakeOffCount.getText(),
                    additionalRotationsCount.getText(),
                    additionalMainChannelFireCount.getText(),
                    additionalOperTimeMinutes.getText())) {
                SavedData.components
                        .stream()
                        .filter(c -> c.equals(createdComponent))
                        .forEach((c ->
                        {
                            ((MPU_MKU) c).setCountOfLandings(
                                    ((MPU_MKU) c).getCountOfLandings()
                                            + Integer.parseInt(additionalTakeOffCount.getText()));
                            ((MPU_MKU) c).setRotationsCount(
                                    ((MPU_MKU) c).getRotationsCount()
                                            + Integer.parseInt(additionalRotationsCount.getText()));
                            ((MPU_MKU) c).setStartsOnMainChannel(
                                    ((MPU_MKU) c).getStartsOnMainChannel()
                                            + Integer.parseInt(additionalMainChannelFireCount.getText()));
                            ((MPU_MKU) c).setStartsOnReserveChannel(
                                    ((MPU_MKU) c).getStartsOnReserveChannel()
                                            + Integer.parseInt(additionalReserveChannelFireCount.getText()));
                            ((MPU_MKU) c).setFlightOperatingTime(
                                    ((MPU_MKU) c).getFlightOperatingTime()
                                            + Integer.parseInt(additionalOperTimeMinutes.getText()) +
                                            Integer.parseInt(additionalOperTimeHours.getText()) * 60);

                        }));
                SavedData.saveCurrentStateData();
                controller.updateComponentsList();
                createdComponent = SavedData.components.get(id);
                fillComponentInfo();
                resetAdditionalFieldAfterAdd();
                showWarning("Наработка успешно добавлена.");
            } else {
                showWarning("Проверьте, что все поля заполнены. \nЕсли изменений не было, введите в это поле 0.");
            }
        }
    }

    private void handleUnmountCheckboxSelected() {
        if (isUnmounted.isSelected()) {
            componentAttachedTo.setOpacity(.5);
            componentAttachedTo.setEffect(new GaussianBlur(5));
            componentAttachedTo.setDisable(true);
            isUnmountedFromAircraft = true;
            SavedData.aircraft.stream().filter(a -> a.getComponents().contains(createdComponent)).forEach(a -> a.getComponents().remove(createdComponent));
        } else {
            componentAttachedTo.setOpacity(1);
            componentAttachedTo.setDisable(false);
            componentAttachedTo.setEffect(null);
            isUnmountedFromAircraft = false;
        }
    }

    private void resetAdditionalFieldAfterAdd() {
        additionalRotationsCount.setText("");
        additionalOperTimeMinutes.setText("");
        additionalTakeOffCount.setText("");
        additionalOperTimeHours.setText("");
        additionalReserveChannelFireCount.setText("");
        additionalMainChannelFireCount.setText("");
    }

    private MPU_MKU createMPU_MKU(ComponentType type) {
        int flightTime = Integer.parseInt(hoursOperTime.getText()) * 60 + Integer.parseInt(minutesOperTime.getText());
        String attachedTo;
        if (isUnmountedFromAircraft) {
            attachedTo = "Снят с ВС. На хранении.";
        } else {
            attachedTo = componentAttachedTo.getSelectionModel().getSelectedItem();

        }
        return new MPU_MKU(
                attachedTo,
                isUnmountedFromAircraft,
                type,
                Integer.parseInt(componentNumber.getText()),
                Integer.parseInt(takeOffCount.getText()),
                Integer.parseInt(mainChannelFireCount.getText()),
                Integer.parseInt(reserveChannelFireCount.getText()),
                flightTime,
                Integer.parseInt(rotationsCount.getText()));
    }

}
