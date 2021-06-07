package controller;

import data.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import utils.TextUtils;

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
    private ChoiceBox<String> aircraftAttachedTo;

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
        log.info(" setAircraft: {}", aircraft);
        this.aircraft = selectedAircraft;
        aircraftNumber.setText(aircraft.getSideNumber());
        aircraftName.setText(aircraft.getName());
        aircraftRegNumber.setText(aircraft.getRegNumber());
        componentsOnAircraft.getItems().addAll(SavedData.getAircraft(selectedAircraft.toString()).getComponents());
        aircraftAttachedTo.setValue(aircraft.getEngineer());

    }

    @FXML
    void initialize() {
        log.info(" инициализация.");
        // повесим ограничения на поля, чтобы при редактировании нельзя было ввести недопустимые данные
        aircraftNumber.setTextFormatter(createFormatterForOnlyDigits(2));
        aircraftName.setTextFormatter(createFormatterForOnlyText("^(?i)[А-Я,а-я? ]{0,40}$"));
        aircraftRegNumber.setTextFormatter(createFormatterForOnlyText("^(?i)rf?-?[0-9]{0,7}"));
        aircraftAttachedTo.getItems().addAll(SavedData.engineers.stream().map(Engineer::toString).collect(Collectors.toList()));
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
        log.info(" openPersonalComponentPage");
        MainController mainController = aircraftTabController.getMainController();
        mainController.getAircraftComponentsTabController().openPersonalComponentPage(mainController.getAircraftComponentsTabController());
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
        log.info(" handleSaveChangesButton");

        if (TextUtils.checkInputText(aircraftNumber.getText(),
                aircraftName.getText(),
                aircraftRegNumber.getText())
                && aircraftAttachedTo.getSelectionModel().getSelectedItem() != null
                && TextUtils.checkAircraftRegNumber(aircraftRegNumber.getText())
        ) {
            SavedData.aircraft.stream().filter(a -> a.equals(aircraft)).forEach(a -> {
                a.setEngineer(aircraftAttachedTo.getSelectionModel().getSelectedItem());
                a.setName(aircraftName.getText());
                a.setRegNumber(aircraftRegNumber.getText());
                a.setSideNumber(aircraftNumber.getText());
            });
            SavedData.saveCurrentStateData();
            aircraftTabController.updateAircraftsList();
        }
    }

}
