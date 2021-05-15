package controller;

import data.Engineer;
import data.SavedData;
import data.enums.Rank;
import gui.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

public class EngineersTabController {
    @FXML
    @Getter
    private TableView<Engineer> listOfEngineers;
    @FXML
    private TableColumn<String, String> rankColumn;
    @FXML
    private TableColumn<String, String> fullNameColumn;
    @FXML
    private ChoiceBox<Rank> militaryRank;
    @FXML
    private TextField fullEngineersName;

    @FXML
    void initialize() {
        SavedData.readDataFromSave();
        updateEngineersList();
        listOfEngineers.setOnMouseClicked(this::showEngineerEditDialog);
        fullEngineersName.setText("");
        militaryRank.setItems(prepareMilitaryRanks());
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }


    ObservableList<Rank> prepareMilitaryRanks() {
        militaryRank.setTooltip(new Tooltip("Выбери звание инженера!"));
        return FXCollections.observableList(new ArrayList<>(EnumSet.allOf(Rank.class)));
    }

    void updateEngineersList() {
        if (SavedData.engineers.size() == 0) {
            listOfEngineers.setPlaceholder(new Label("Не введены данные об инженерах.\n" +
                    "Добавьте хотя бы одного человека."));
        } else if (listOfEngineers != null) {
            listOfEngineers.getItems().clear();
            for (Engineer engineer : SavedData.engineers) {
                listOfEngineers.getItems().addAll(engineer);
            }
        }
    }

    public void showEngineerEditDialog(MouseEvent e) {
        fillSelectedEngineerInfo();
        if (e.getClickCount() == 2) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/personalEngineerPage.fxml"));
                TitledPane page = loader.load();
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                PersonalEngineerPageController controller = loader.getController();
                controller.setEngineer(listOfEngineers.getSelectionModel().getSelectedItem());
                dialogStage.show();
                SavedData.readDataFromSave();
                updateEngineersList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    void fillSelectedEngineerInfo() {
        militaryRank.setValue(listOfEngineers.getSelectionModel().getSelectedItem().getRank());
        fullEngineersName.setText(listOfEngineers.getSelectionModel().getSelectedItem().getFullName());
    }

    @FXML
    void addEngineer() {
        if (StringUtils.isNotBlank(fullEngineersName.getCharacters()) && militaryRank.getSelectionModel().getSelectedItem() != null) {
            Engineer engineer = new Engineer(
                    militaryRank.getSelectionModel().getSelectedItem(),
                    fullEngineersName.getText(),
                    new ArrayList<>());
            listOfEngineers.getItems().addAll(engineer);
            SavedData.engineers.add(engineer);
            SavedData.saveCurrentStateData();

        }
    }

    @FXML
    void deleteEngineer() {
        SavedData.engineers.remove(listOfEngineers.getSelectionModel().getSelectedItem());
        SavedData.saveCurrentStateData();
        listOfEngineers.getItems().remove(listOfEngineers.getSelectionModel().getSelectedItem());
        listOfEngineers.refresh();
    }

    @FXML
    void editEngineer() {
        if (StringUtils.isNotBlank(fullEngineersName.getCharacters())
                && militaryRank.getSelectionModel().getSelectedItem() != null) {
            listOfEngineers.getSelectionModel().getSelectedItem().setFullName(fullEngineersName.getText());
            listOfEngineers.getSelectionModel().getSelectedItem().setRank(militaryRank.getSelectionModel().getSelectedItem());
        }
        SavedData.saveCurrentStateData();
        updateEngineersList();
    }
}

