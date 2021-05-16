package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import lombok.Getter;

public class MainController {
    @FXML
    Tab aircraftTabButton;

    @FXML
    Tab engineerTabButton;

    @FXML
    @Getter
    AircraftTabController aircraftTabController;

    @FXML
    @Getter
    EngineersTabController engineersTabController;

    @FXML
    @Getter
    AircraftComponentsTabController aircraftComponentsTabController;

    @FXML
    void updateAircraftTab() {
        aircraftTabController.updateAircraftsList();
        aircraftTabController.updateEngineersListOnAircraftTab();
    }

    @FXML
    void updateEngineerTab() {
        engineersTabController.updateEngineersList();
    }

    @FXML
    void updateComponentsTab() {
        aircraftComponentsTabController.updateComponentsList();
    }

    @FXML
    void initialize() {
        aircraftTabController.setMainController(this);
        aircraftComponentsTabController.setMainController(this);
    }

}
