package controller;

import data.Aircraft;
import data.Engineer;
import data.SavedData;
import gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Optional;

public class PersonalEngineerPageController {

    @FXML
    public Button attachAircraftButton;
    @FXML
    public Button unattachAircraftButton;
    @FXML
    private Label rankField;
    @FXML
    private Label fullNameField;
    @FXML
    private ListView<Aircraft> attachedAircraftList;
    @Setter
    private EngineersTabController engineersTabController;
    private Engineer engineer;

    public void setEngineer(Engineer engineer) {
        this.engineer = engineer;
        rankField.setText(engineer.getRank().getDescription());
        fullNameField.setText(engineer.getFullName());
        attachedAircraftList.getItems().clear();
        attachedAircraftList.getItems().addAll(engineer.getAttachedAircrafts());
    }

    public void unattachAircraft() {
        // читаем данные из сейвов
        SavedData.readDataFromSave();
        // считываем какой борт выбран
        Aircraft aircraftToUnattach = attachedAircraftList.getSelectionModel().getSelectedItem();
        // т.к. борт откреплен от ИАК, заменяем для наглядности запись, чтобы в списке ВС было легко заметить где чье.
        SavedData.aircraft.stream()
                .filter(a -> a.getEngineer().equals(engineer.toString()))
                .forEach(a -> a.setEngineer("За данным ВС не закреплен ни один инженер СПиСО"));
        // для инженера, имеющего этот борт в закрепленных снимаем связь
        SavedData.engineers.stream()
                .filter(e -> e.getAttachedAircrafts().contains(aircraftToUnattach))
                .forEach(e -> e.getAttachedAircrafts().remove(aircraftToUnattach));
        attachedAircraftList.getItems().remove(aircraftToUnattach);
        SavedData.saveCurrentStateData();
        engineersTabController.updateEngineersList();

    }

    private void attachAircraft(Aircraft aircraft) {
        // читаем данные из сейвов
        SavedData.readDataFromSave();
        aircraft.setEngineer(engineer.toString());
        // удаляем из списка прикрепленных самолетов этот борт у других инженеров
        SavedData.engineers.stream().filter(e -> !e.equals(engineer)).forEach(e -> e.getAttachedAircrafts().remove(aircraft));
        // для текущего борта устанавливаем выбранного инженера
        SavedData.aircraft.stream()
                .filter(a -> a.equals(aircraft))
                .forEach(a -> a.setEngineer(engineer.toString()));
        // для выбранного инженера добавляем этот борт в прикрепленные, если такой уже есть - идем дальше.
        SavedData.engineers.stream().filter(e -> e.equals(engineer)).forEach(e -> {
            if (!e.getAttachedAircrafts().contains(aircraft)) {
                e.getAttachedAircrafts().add(aircraft);
                engineer.getAttachedAircrafts().clear();
                engineer.getAttachedAircrafts().addAll(e.getAttachedAircrafts());
            }
        });
        attachedAircraftList.getItems().clear();
        attachedAircraftList.getItems().addAll(engineer.getAttachedAircrafts());
        // сохраняем правки на компьютер
        SavedData.saveCurrentStateData();
        engineersTabController.updateEngineersList();
    }

    public void showAttachAircraftDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/attachAircraftToEngineer.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            AttachAircraftToEngineerController controller = loader.getController();
            Optional<Aircraft> selectedAircraft = controller.handleClickAttachButton();
            selectedAircraft.ifPresent(this::attachAircraft);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}