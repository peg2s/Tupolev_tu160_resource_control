package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainController {
    @FXML
    Tab aircraftTabButton;

    @FXML
    Tab engineerTabButton;

    @FXML
    AircraftTabController aircraftTabController;

    @FXML
    void updateAircraftTab() {
        aircraftTabController.initialize();
    }

}
