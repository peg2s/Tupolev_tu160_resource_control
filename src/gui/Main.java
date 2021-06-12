package gui;

import data.TextConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        log.info("Запуск программы");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle(TextConstants.TITLE);
        primaryStage.setScene(new Scene(root, 1200, 760));
        primaryStage.getIcons().add(new Image("/images/logo.jpg"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
