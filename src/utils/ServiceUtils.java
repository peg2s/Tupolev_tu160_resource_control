package utils;

import data.Aircraft;
import data.Component;
import data.SavedData;
import data.enums.ComponentType;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceUtils {
    public static void showWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Проверка, чтобы на один борт нельзя было добавить больше заданного производителем агрегатов,
     * а также проверка на несовместимость уже установленных агрегатов с создаваемым.
     * Кроме этого проверяется номер, он должен быть уникальным.
     *
     * @param aircraft
     * @param component
     * @return разрешено ли добавление
     */
    public static boolean checkComponentsOnAircraft(Aircraft aircraft, Component component) {
        if (SavedData.components.stream().anyMatch(c -> c.getNumber().equals(component.getNumber()))) {
            showWarning("Агрегат с таким номером уже существует. Проверьте вводимые данные.");
            return false;
        }
        List<String> componentsOnAircrat = aircraft.getComponents();
        int mpuCount = componentsOnAircrat.stream().filter(c -> c.contains(ComponentType.MPU.getName())).collect(Collectors.toList()).size();
        int mkuCount = componentsOnAircrat.stream().filter(c -> c.contains(ComponentType.MKU.getName())).collect(Collectors.toList()).size();
        ComponentType type = component.getType();
        if (componentsOnAircrat.size() == 0) {
            return true;
        } else if ((type == ComponentType.MKU && mpuCount != 0)
                || (type == ComponentType.MPU && mkuCount != 0)) {
            showWarning("Запрещена одновременная установка изд. 9А-829К2 и 9А-829К3");
            return false;
        } else if ((type == ComponentType.MKU && mkuCount == 2)
                || (type == ComponentType.MPU && mpuCount == 2)) {
            showWarning("На ВС уже установлено максимально допустимое кол-во изд." + type.getName());
            return false;
        }
        return true;
    }
}
