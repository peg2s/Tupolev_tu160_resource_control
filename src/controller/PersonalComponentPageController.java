package controller;

import data.Aircraft;
import data.Component;
import data.MKU;
import data.SavedData;
import data.enums.ComponentType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.function.UnaryOperator;

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
        createButton.setOnAction(e -> handleClickCreateButton());
    }

    void createComponent() {
        if (componentType.getSelectionModel().getSelectedItem() == ComponentType.L029) {
        // todo: отрисовка иной формы ввода для данного типа компонент
        } else if (componentType.getSelectionModel().getSelectedItem() != null) {
            if (StringUtils.isNotBlank(takeOffCount.getText())
                    && StringUtils.isNotBlank(mainChannelFireCount.getText())
                    && StringUtils.isNotBlank(reserveChannelFireCount.getText())
                    && StringUtils.isNotBlank(hoursOperTime.getText())
                    && StringUtils.isNotBlank(minutesOperTime.getText())
                    && StringUtils.isNotBlank(takeOffCount.getText())
                    && StringUtils.isNotBlank(rotationsCount.getText())
                    && StringUtils.isNotBlank(componentNumber.getText())) {
                int flightTime = Integer.parseInt(hoursOperTime.getText()) * 60 + Integer.parseInt(minutesOperTime.getText());
                createdComponent = new MKU(Integer.parseInt(componentNumber.getText()),
                        Integer.parseInt(takeOffCount.getText()),
                        Integer.parseInt(mainChannelFireCount.getText()),
                        Integer.parseInt(reserveChannelFireCount.getText()),
                        flightTime,
                        componentAttachedTo.getSelectionModel().getSelectedItem().toString());

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

    TextFormatter<Integer> createFormatter() {
        UnaryOperator<Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };
        return new TextFormatter<Integer>(integerFilter);
    }

    void setParentController(AircraftComponentsTabController controller) {
        this.controller = controller;
    }

}
