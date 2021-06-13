package utils;

import data.TextConstants;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.UnaryOperator;

import static utils.ServiceUtils.showWarning;

@Slf4j
public class TextUtils {

    /**
     * Создание ограничений на ввод данных в TextField
     *
     * @param maxLength - максимально разрешенное число символов
     * @return TextFormatter с установленными ограничениями
     */
    public static TextFormatter<Integer> createFormatterForOnlyDigits(int maxLength) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^[0-9]{0," + maxLength + "}$")) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(integerFilter);
    }

    /**
     * Создание ограничений на ввод данных в поля, содержащие минуты.
     * Диапазон данных 0-9 минут или 0-59 минут
     *
     * @return TextFormatter с установленными ограничениями
     */
    public static TextFormatter<Integer> createFormatterForMinutes() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("(^[0-5][0-9]{0,1})|^$|^[0-9]$")) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(integerFilter);
    }

    /**
     * Создание ограничений на ввод данных в поля, содержащие текст
     *
     * @param regexp - регулярка, по которой проверяется вводимый текст
     * @return TextFormatter с установленными ограничениями
     */
    public static TextFormatter<String> createFormatterForOnlyText(String regexp) {
        UnaryOperator<TextFormatter.Change> textFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(regexp)) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(textFilter);
    }

    /**
     * Проверка возможности добавления введенной наработки за летную смену
     *
     * @param hours - часы
     * @param minutes - минуты
     * @param rotations - число поворотов
     * @param fireMain - пуски по осн. каналу
     * @param fireReserve - пуски по рез. каналу
     * @param takeOffs - число взлёт-посадок
     * @return разрешено ли добавление
     *
     */
    public static boolean checkAdditionalOperating(TextField hours, TextField minutes,
                                                   TextField rotations, TextField fireMain,
                                                   TextField fireReserve, TextField takeOffs) {
        int addHours = Integer.parseInt(hours.getText());
        int addMinutes = Integer.parseInt(minutes.getText());
        int addRotations;
        try {
            addRotations = Integer.parseInt(rotations.getText());
        } catch (Exception e) {
            addRotations = 0;
        }
        int addFireMain = Integer.parseInt(fireMain.getText());
        int addFireReserve = Integer.parseInt(fireReserve.getText());
        int addTakeOffs = Integer.parseInt(takeOffs.getText());
        if (addFireMain > 12
                || addFireReserve > 12
                || addHours > 50
                || addMinutes > 99
                || addRotations > 12
                || addTakeOffs > 20) {
            ServiceUtils.showWarning("Введенная наработка содержит числа,\nневозможные за одну лётную смену.");
            return false;
        }
        return true;
    }

    /**
     *  Проверка вводимого текста на пустое поле
     *
     * @param textFields - массив вводимых данных
     * @return пройдена ли проверка
     */
    public static boolean checkInputText(TextField... textFields) {
        for (TextField t : textFields) {
            log.info("В поле {} введено значение {}", t.getId() , t.getText());
            if (StringUtils.isBlank(t.getText())) {
                showWarning(TextConstants.EMPTY_FIELD_WARNING);
                return false;
            }
        }
        return true;
    }

    /**
     *  Проверка рег.номера ВС на длину.
     *
     * @param regNumber - рег.номер самолета
     * @return пройдена ли проверка
     */
    public static boolean checkAircraftRegNumber(String regNumber) {
        if (regNumber.length() < 5) {
            ServiceUtils.showWarning(TextConstants.REG_NUMBER_CHECK);
            return false;
        }
        return true;
    }

}
