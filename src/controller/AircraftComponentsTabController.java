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

import java.io.IOException;

public class AircraftComponentsTabController {
    @FXML
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

    private Component selectedComponent;

    @FXML
    void initialize() {
        SavedData.readDataFromSave();
        componentsList.getItems().addAll(SavedData.components);
        addComponentButton.setOnAction(e->openPersonalComponentPage());
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
    void openPersonalComponentPage() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/personalComponentPage.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            PersonalComponentPageController controller = loader.getController();
            controller.setParentController(this);
            dialogStage.show();
            SavedData.readDataFromSave();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}

