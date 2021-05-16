package utils;

import javafx.scene.control.Alert;

public class ServiceUtils {
    public static void showWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
