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

    public static boolean checkAircraftRegNumber(String regNumber) {
        if (regNumber.length() < 5) {
            ServiceUtils.showWarning(TextConstants.REG_NUMBER_CHECK);
            return false;
        }
        return true;
    }

}
