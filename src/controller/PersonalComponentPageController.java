package controller;

import data.*;
import data.enums.ComponentType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static data.enums.ComponentType.L029;
import static utils.ServiceUtils.*;
import static utils.TextUtils.*;

@Slf4j
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
    private AnchorPane specificForMkuMpuPane;
    @FXML
    @Setter
    private MainController mainController;
    @Getter
    private Component createdComponent;
    private boolean isUnmountedFromAircraft;
    private Component selectedComponent;
    private AircraftComponentsTabController controller;
    private boolean isEditMode;
    private int componentIdInSavedData;

    @FXML
    void initialize() {
        isUnmounted.setOnAction(e -> handleUnmountCheckboxSelected());
        componentType.setOnAction(e -> hideSpecificFields());
        // установим ограничение на ввод данных: только цифры
        hoursOperTime.setTextFormatter(createFormatterForOnlyDigits(4));
        minutesOperTime.setTextFormatter(createFormatterForMinutes());
        componentNumber.setTextFormatter(createFormatterForOnlyDigits(30));
        rotationsCount.setTextFormatter(createFormatterForOnlyDigits(2));
        mainChannelFireCount.setTextFormatter(createFormatterForOnlyDigits(3));
        reserveChannelFireCount.setTextFormatter(createFormatterForOnlyDigits(3));
        takeOffCount.setTextFormatter(createFormatterForOnlyDigits(4));
        additionalMainChannelFireCount.setTextFormatter(createFormatterForOnlyDigits(3));
        additionalReserveChannelFireCount.setTextFormatter(createFormatterForOnlyDigits(3));
        additionalOperTimeHours.setTextFormatter(createFormatterForOnlyDigits(3));
        additionalOperTimeMinutes.setTextFormatter(createFormatterForMinutes());
        additionalTakeOffCount.setTextFormatter(createFormatterForOnlyDigits(3));
        additionalRotationsCount.setTextFormatter(createFormatterForOnlyDigits(3));

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
        log.info("Окно редактирования компонента {}", component);
        isEditMode = true;
        componentIdInSavedData = SavedData.getComponentId(component);
        this.createdComponent = component;
        this.selectedComponent = component;
        fillComponentInfo();
    }

    void createComponent() {
        // создаем новый агрегат выбранного типа
        if (componentType.getSelectionModel().getSelectedItem() == L029) {
            createdComponent = createL029();
        } else {
            createdComponent = createMPU_MKU(componentType.getValue());
        }
        // проверяем, все ли поля заполнены
        if (checkInputIsNotBlank()) {
            if (isEditMode) {
                if (SavedData.components.get(componentIdInSavedData) instanceof L029) {
                    L029 componentInSave = (L029) SavedData.components.get(componentIdInSavedData);
                    createdComponent.setUuid(componentInSave.getUuid());
                    process(componentInSave);
                } else {
                    MPU_MKU componentInSave = (MPU_MKU) SavedData.components.get(componentIdInSavedData);
                    createdComponent.setUuid(componentInSave.getUuid());
                    process(componentInSave);
                }
            } else {
                process(createdComponent);
            }
            SavedData.saveCurrentStateData();
        }
    }

    private void process(Component component) {
        if (checkComponentsOnAircraft(
                component,
                createdComponent,
                componentIdInSavedData,
                isEditMode)) {

            if (!component.getAttachedToAircraft().equals(createdComponent.getAttachedToAircraft())) {
                SavedData.aircraft.stream().filter(a -> a.toString().equals(
                        (component).getAttachedToAircraft()))
                        .forEach(a -> a.getComponents().remove(selectedComponent.toString())
                        );
                SavedData.aircraft.stream().filter(a -> a.toString().equals(
                        createdComponent.getAttachedToAircraft()))
                        .forEach(a -> a.getComponents().add(createdComponent.toString())
                        );
            } else {
                if (!component.isUnmounted() && !isEditMode) {
                    SavedData.getAircraft(component.getAttachedToAircraft()).getComponents().add(createdComponent.toString());
                }
            }
            if (isEditMode) {
                SavedData.components.set(componentIdInSavedData, createdComponent);
            } else {
                SavedData.components.add(createdComponent);
            }
        }
        log.info("createComponent. Созданный компонент: {},\n режим редактирования {},\n установлен на ВС {}",
                createdComponent, isEditMode, createdComponent.getAttachedToAircraft());
    }


    @FXML
    public void handleClickCreateButton() {
        createComponent();
        if (createdComponent != null) {
            controller.setSelectedComponent(createdComponent);
        }
        if (checkInputIsNotBlank()) {
            ((Stage) createButton.getScene().getWindow()).close();
        } else {
            showWarning(TextConstants.CHECK_ALL_FIELDS_FILLED);
        }
        log.info("handleClickCreateButton, нажата кнопка создать");
    }

    public void setParentController(AircraftComponentsTabController controller) {
        this.controller = controller;
    }

    boolean checkInputIsNotBlank() {
        boolean checkOk = checkInputText(componentNumber, hoursOperTime,
                minutesOperTime, takeOffCount,
                mainChannelFireCount, reserveChannelFireCount)
                && componentType.getSelectionModel().getSelectedItem() != null
                && (componentAttachedTo.getSelectionModel().getSelectedItem() != null || isUnmountedFromAircraft);

        if (createdComponent.getType() == L029) {
            if (!checkOk) {
                showWarning(TextConstants.EMPTY_FIELD_WARNING);
                return false;
            }
            return true;
        } else {
            if (!checkOk && checkInputText(rotationsCount)) {
                return false;
            }
            return checkOk && checkInputText(rotationsCount);
        }
    }

    void fillComponentInfo() {
        hoursOperTime.setText(String.valueOf(createdComponent.getFlightOperatingTime() / 60));
        minutesOperTime.setText(String.valueOf(createdComponent.getFlightOperatingTime() % 60));
        reserveChannelFireCount.setText(String.valueOf(createdComponent.getStartsOnReserveChannel()));
        mainChannelFireCount.setText(String.valueOf(createdComponent.getStartsOnMainChannel()));
        takeOffCount.setText(String.valueOf(createdComponent.getCountOfLandings()));
        componentNumber.setText(String.valueOf(createdComponent.getNumber()));
        componentType.setValue(createdComponent.getType());
        isUnmounted.setSelected(createdComponent.isUnmounted());
        if (createdComponent instanceof MPU_MKU) {
            rotationsCount.setText(String.valueOf(((MPU_MKU) (createdComponent)).getRotationsCount()));
        }
            isUnmounted.setSelected(createdComponent.isUnmounted());
            handleUnmountCheckboxSelected();
            if (!createdComponent.isUnmounted()) {
                componentAttachedTo.setValue(SavedData.aircraft.stream()
                        .filter(a -> a.toString()
                                .equals(createdComponent
                                        .getAttachedToAircraft()))
                        .findAny().get().toString());
            }
        hideSpecificFields();
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
        log.info("setAdditionalFieldsDisabled, значение {}", isDisabled);
    }

    private void addLastFlightInfo() {
        int id = SavedData.getComponentId(createdComponent);
        if (check(createdComponent)) {
            SavedData.components
                    .stream()
                    .filter(c -> c.equals(createdComponent))
                    .forEach((c ->
                    {
                        c.setCountOfLandings(
                                c.getCountOfLandings()
                                        + Integer.parseInt(additionalTakeOffCount.getText()));
                        if (createdComponent instanceof MPU_MKU) {
                            ((MPU_MKU) c).setRotationsCount(
                                    ((MPU_MKU) c).getRotationsCount()
                                            + Integer.parseInt(additionalRotationsCount.getText()));
                        }
                        c.setStartsOnMainChannel(
                                c.getStartsOnMainChannel()
                                        + Integer.parseInt(additionalMainChannelFireCount.getText()));
                        c.setStartsOnReserveChannel(
                                c.getStartsOnReserveChannel()
                                        + Integer.parseInt(additionalReserveChannelFireCount.getText()));
                        c.setFlightOperatingTime(
                                c.getFlightOperatingTime()
                                        + Integer.parseInt(additionalOperTimeMinutes.getText()) +
                                        Integer.parseInt(additionalOperTimeHours.getText()) * 60);

                    }));
            SavedData.saveCurrentStateData();
            controller.updateComponentsList();
            createdComponent = SavedData.components.get(id);
            fillComponentInfo();
            log.info("добавлена наработка для компонента {} \n" +
                            " часы {} \n " +
                            "минуты {} \n " +
                            "посадки {} \n " +
                            "число поворотов {} \n " +
                            "число пусков осн. {} \n " +
                            "число пусков рез. {} \n",
                    createdComponent,
                    additionalOperTimeHours.getText(),
                    additionalOperTimeMinutes.getText(),
                    additionalTakeOffCount.getText(),
                    StringUtils.isNotBlank(additionalRotationsCount.getText())
                            ? additionalRotationsCount.getText() : TextConstants.NO_COMPLIANT_FOR_L029,
                    additionalMainChannelFireCount.getText(),
                    additionalReserveChannelFireCount.getText());
            resetAdditionalFieldAfterAdd();
            showWarning(TextConstants.ADDITIONAL_OPERATING_ADDED);
        }
    }

    private void handleUnmountCheckboxSelected() {
        if (isUnmounted.isSelected()) {
            log.info("handleUnmountCheckboxSelected");
            componentAttachedTo.setOpacity(.5);
            componentAttachedTo.setEffect(new GaussianBlur(5));
            componentAttachedTo.setDisable(true);
            isUnmountedFromAircraft = true;
            SavedData.aircraft.stream()
                    .filter(a -> a.getComponents().contains(createdComponent))
                    .forEach(a -> a.getComponents().remove(createdComponent));
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
            attachedTo = TextConstants.UNATTACHED_FROM_AIRCRAFT;
        } else {
            attachedTo = componentAttachedTo.getSelectionModel().getSelectedItem();

        }
        log.info("Cоздаем изд. {}", type);

        return new MPU_MKU(
                attachedTo,
                isUnmountedFromAircraft,
                type,
                new BigDecimal(componentNumber.getText()),
                Integer.parseInt(takeOffCount.getText()),
                Integer.parseInt(mainChannelFireCount.getText()),
                Integer.parseInt(reserveChannelFireCount.getText()),
                flightTime,
                Integer.parseInt(rotationsCount.getText()));
    }

    private L029 createL029() {
        int flightTime = Integer.parseInt(hoursOperTime.getText()) * 60 + Integer.parseInt(minutesOperTime.getText());
        String attachedTo;
        if (isUnmountedFromAircraft) {
            attachedTo = TextConstants.UNATTACHED_FROM_AIRCRAFT;
        } else {
            attachedTo = componentAttachedTo.getSelectionModel().getSelectedItem();

        }
        log.info("Cоздаем изд. Л-029");

        return new L029(
                attachedTo,
                isUnmountedFromAircraft,
                L029,
                new BigDecimal(componentNumber.getText()),
                Integer.parseInt(takeOffCount.getText()),
                Integer.parseInt(mainChannelFireCount.getText()),
                Integer.parseInt(reserveChannelFireCount.getText()),
                flightTime);
    }

    private void hideSpecificFields() {
        specificForMkuMpuPane.setVisible(componentType.getSelectionModel().getSelectedItem() != L029);
    }

    private boolean check(Component component) {
        boolean checkInputOk = checkInputText(additionalOperTimeHours, additionalOperTimeMinutes,
                additionalMainChannelFireCount, additionalReserveChannelFireCount, additionalTakeOffCount);
        if (component instanceof MPU_MKU) {
            return checkInputOk && checkInputText(rotationsCount)
                    && checkAdditionalOperating(additionalOperTimeHours, additionalOperTimeMinutes,
                    additionalRotationsCount, additionalMainChannelFireCount, additionalReserveChannelFireCount,
                    additionalTakeOffCount);
        } else {
            return checkInputOk
                    && checkAdditionalOperating(additionalOperTimeHours, additionalOperTimeMinutes,
                    null, additionalMainChannelFireCount,
                    additionalReserveChannelFireCount, additionalTakeOffCount);
        }
    }

}
