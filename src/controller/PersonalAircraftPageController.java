package controller;

import data.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import static utils.TextUtils.createFormatterForOnlyDigits;
import static utils.TextUtils.createFormatterForOnlyText;

@Slf4j
public class PersonalAircraftPageController {

    @FXML
    private TextField aircraftNumber;

    @FXML
    private TextField aircraftName;

    @FXML
    private TextField aircraftRegNumber;

    @FXML
    private ChoiceBox<Engineer> aircraftAttachedTo;

    @FXML
    @Getter
    private ListView<String> componentsOnAircraft;

    @FXML
    private Button createNewComponent;

    @FXML
    private Button unattachSelectedComponent;

    @FXML
    private Button saveChanges;

    private Aircraft aircraft;
    @Setter
    private AircraftTabController aircraftTabController;

    public void setAircraft(Aircraft selectedAircraft) {
        this.aircraft = selectedAircraft;
        aircraftNumber.setText(aircraft.getSideNumber());
        aircraftName.setText(aircraft.getName());
        aircraftRegNumber.setText(aircraft.getRegNumber().replaceAll("RF-", ""));
        componentsOnAircraft.getItems().addAll(SavedData.getAircraft(selectedAircraft.toString()).getComponents());
        aircraftAttachedTo.setValue(SavedData.getEngineerFromList(aircraft.getEngineer()));

    }

    @FXML
    void initialize() {
        // повесим ограничения на поля, чтобы при редактировании нельзя было ввести недопустимые данные
        aircraftNumber.setTextFormatter(createFormatterForOnlyDigits(2));
        aircraftName.setTextFormatter(createFormatterForOnlyText("^(?i)[А-Я,а-я? ]{0,40}$"));
        aircraftRegNumber.setTextFormatter(createFormatterForOnlyText("^[0-9]{0,5}"));
        aircraftAttachedTo.getItems().addAll(FXCollections.observableList(SavedData.engineers));
        unattachSelectedComponent.setOnAction(e -> unattachComponent());
        createNewComponent.setOnAction(e -> openPersonalComponentPage());
        saveChanges.setOnAction(e -> handleSaveChangesButton());
    }

    private void unattachComponent() {
        String component = componentsOnAircraft.getSelectionModel().getSelectedItem();
        log.info(" unattachComponent: {}", component);
        aircraft.getComponents().remove(component);
        SavedData.components.stream().filter(c -> c.toString().equals(component)).forEach(c -> {
            c.setAttachedToAircraft(TextConstants.UNATTACHED_FROM_AIRCRAFT);
            c.setUnmounted(true);
        });
        SavedData.aircraft.stream().filter(a -> a.getComponents().contains(component)).forEach(a -> a.getComponents().remove(component));
        componentsOnAircraft.getItems().remove(component);
        SavedData.saveCurrentStateData();
    }

    @FXML
    @SneakyThrows
    void openPersonalComponentPage() {
        log.info("Открываем карточку создания или редактирования агрегата");
        MainController mainController = aircraftTabController.getMainController();
        PersonalComponentPageController componentPageController = mainController.getAircraftComponentsTabController()
                .openPersonalComponentPage(mainController.getAircraftComponentsTabController());
        componentPageController.setPersonalAircraftPageController(this);
    }

    public void updateComponentsList() {
        log.info(" updateComponentsList");
        componentsOnAircraft.getItems().clear();
        componentsOnAircraft.getItems().addAll(SavedData.components
                .stream()
                .filter(component -> component.getAttachedToAircraft().equals(aircraft.toString()))
                .map(Component::toString)
                .collect(Collectors.toList()));
    }

    private void handleSaveChangesButton() {
        log.info(" Изменяем информацию по ВС");
        aircraftTabController.edit(aircraftNumber, aircraftRegNumber, aircraftName, aircraftAttachedTo, aircraft);
    }

}
