package controller;

import data.Component;
import data.SavedData;
import data.enums.ComponentType;
import gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AircraftComponentsTabController {
    @FXML
    @Getter
    private TableView<Component> componentsList;

    @FXML
    private TableColumn<Integer, Integer> componentIdColumn;

    @FXML
    private TableColumn<ComponentType, ComponentType> componentTypeColumn;

    @FXML
    private TableColumn<String, String> componentAttachedToColumn;

    @FXML
    private Button addComponentButton;

    @FXML
    private Button deleteComponentButton;

    @FXML
    @Setter
    private MainController mainController;

    private Component selectedComponent;

    @FXML
    void initialize() {
        log.info("инициализация.");

        SavedData.readDataFromSave();
        componentsList.getItems().addAll(SavedData.components);
        componentsList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                PersonalComponentPageController personalComponentPageController = openPersonalComponentPage(this);
                personalComponentPageController.prepareComponentToEdit(componentsList.getSelectionModel().getSelectedItem());
                personalComponentPageController.setAdditionalFieldsDisabled(false);
            }
        });
        addComponentButton.setOnAction(e -> openPersonalComponentPage(this));
        deleteComponentButton.setOnAction(e->deleteSelectedComponent());
        componentIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        componentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        componentAttachedToColumn.setCellValueFactory(new PropertyValueFactory<>("attachedToAircraft"));
    }

    @FXML
    void setSelectedComponent(Component component) {
        this.selectedComponent = component;
        componentsList.getItems().addAll(selectedComponent);
        componentsList.refresh();
    }

    @FXML
    @SneakyThrows
    public PersonalComponentPageController openPersonalComponentPage(AircraftComponentsTabController aircraftComponentsTabController) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/personalComponentPage.fxml"));
        AnchorPane page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
        PersonalComponentPageController controller = loader.getController();
        controller.setParentController(aircraftComponentsTabController);
        controller.setMainController(mainController);
        dialogStage.show();
        SavedData.readDataFromSave();
        return controller;
    }

    private void deleteSelectedComponent() {
        Component component = componentsList.getSelectionModel().getSelectedItem();
        SavedData.aircraft.stream()
                .filter(a->a.getComponents().contains(component.toString()))
                .forEach(a->a.getComponents().remove(component.toString()));
        SavedData.components.remove(component);
        componentsList.getItems().remove(component);
        SavedData.saveCurrentStateData();
    }

    void updateComponentsList() {
        SavedData.readDataFromSave();
        componentsList.getItems().clear();
        componentsList.getItems().addAll(SavedData.components);
    }


}

