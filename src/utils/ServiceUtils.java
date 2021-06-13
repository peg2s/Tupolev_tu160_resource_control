package utils;

import data.Aircraft;
import data.Component;
import data.SavedData;
import data.TextConstants;
import data.enums.ComponentType;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServiceUtils {

    /**
     * Метод выводит диалоговое окно с переданным текстом.
     *
     * @param text Выводимый текст
     */
    public static void showWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TextConstants.ATTENTION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Проверка, чтобы на один борт нельзя было добавить больше заданного производителем агрегатов,
     * а также проверка на несовместимость уже установленных агрегатов с создаваемым.
     * Кроме этого проверяется номер, он должен быть уникальным.
     *
     * @param component        агрегат из сохранения
     * @param createdComponent созданный агрегат
     * @param id               айдишник агрегата в сохранении
     * @param isEditMode       маркер создания или редактирования
     * @return разрешено ли добавление
     */
    public static boolean checkComponentsOnAircraft(Component component, Component createdComponent,
                                                    int id, boolean isEditMode) {
        if (checkComponentDuplicate(component, createdComponent, id)) {
            Aircraft aircraft = SavedData.getAircraft(createdComponent.getAttachedToAircraft());
            // если агрегат не установлен на ВС, то нет ограничений
            if (aircraft == null) {
                return true;
            }
            List<String> componentsOnAircraft = aircraft.getComponents();
            // проверим кол-во агрегатов всех типов на этом ВС
            int mpuCount = (int) componentsOnAircraft.stream().filter(c -> c.contains(ComponentType.MPU.getName())).count();
            int mkuCount = (int) componentsOnAircraft.stream().filter(c -> c.contains(ComponentType.MKU.getName())).count();
            int l029Count = (int) componentsOnAircraft.stream().filter(c -> c.contains(ComponentType.L029.getName())).count();
            ComponentType type = component.getType();
            // если самолет пустой, то нет ограничений
            if (componentsOnAircraft.size() == 0) {
                return true;
                // проверяем на попытку установить несовместимые агрегаты одновременно
            } else if ((type == ComponentType.MKU && mpuCount != 0)
                    || (type == ComponentType.MPU && mkuCount != 0)) {
                showWarning(TextConstants.SIMULTANEOUSLY_INSTALLATION_MKU_MPU);
                return false;
                // проверяем текущее кол-во агрегатов всех типов, уже стоящих на ВС
            } else if ((type == ComponentType.MKU && mkuCount == 2)
                    || (type == ComponentType.MPU && mpuCount == 2)
                    || (type == ComponentType.L029 && l029Count == 3)) {
                // исключаем из проверки агрегат, который редактируется на данном ВС
                if (!component.getAttachedToAircraft().equals(aircraft.toString()) && isEditMode) {
                    showWarning(TextConstants.MAX_COUNT_OF_COMPONENTS + type.getName());
                    return false;
                }
                if (createdComponent.getAttachedToAircraft().equals(aircraft.toString()) && !isEditMode) {
                    showWarning(TextConstants.MAX_COUNT_OF_COMPONENTS + type.getName());
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Проверка агрегата на дубликат
     *
     * @param component агрегат до редактирования
     * @param createdComponent созданный или отредактированных агрегат
     * @param id айди агрегата в сохранении (если есть)
     * @return разрешено ли добавление или изменение агрегата
     */
    public static boolean checkComponentDuplicate(Component component,
                                                  Component createdComponent,
                                                  int id) {
        if (SavedData.components.size() != 0
                && !SavedData.components.get(id).equals(createdComponent)
                && SavedData.components.stream().anyMatch(c -> c.getNumber().equals(createdComponent.getNumber()))) {
            showWarning(TextConstants.COMPONENT_ALREADY_EXISTS);
            return false;
        }
        if (component.equals(createdComponent) && component.getNumber().equals(createdComponent.getNumber())) {
            return true;
        }
        if (component.equals(createdComponent)
                && SavedData.components.stream().anyMatch(c -> c.getNumber().equals(createdComponent.getNumber()))) {
            showWarning(TextConstants.COMPONENT_ALREADY_EXISTS);
            return false;
        }
        return true;
    }

    /**
     * Проверка самолета на дубликат при создании и редактировании
     *
     * @param aircraft - создаваемый или редактируемый самолет
     * @return возможно ли редактирование или создание
     */
    public static boolean checkAircraftDuplicate(Aircraft aircraft) {
        boolean isCheckFailed = SavedData.aircraft
                .stream()
                .anyMatch(a -> a.getSideNumber().equals(aircraft.getSideNumber())
                        || a.getRegNumber().equals(aircraft.getRegNumber())
                        || a.getName().equalsIgnoreCase(aircraft.getName()));
        if (isCheckFailed) {
            showWarning(TextConstants.AIRCRAFT_DUPLICATE);
        }
        return isCheckFailed;
    }
}
