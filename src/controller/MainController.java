package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainController {
    @FXML
    Tab aircraftTabButton;

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
        log.info("Выбрана вкладка \"Список ВС\"");
    }

    @FXML
    void updateEngineerTab() {
        engineersTabController.updateEngineersList();
        log.info("Выбрана вкладка \"Список ИАК\"");
    }

    @FXML
    void updateComponentsTab() {
        aircraftComponentsTabController.updateComponentsList();
        log.info("Выбрана вкладка \"Список агрегатов\"");
    }

    @FXML
    void initialize() {
        aircraftTabController.setMainController(this);
    }


}
