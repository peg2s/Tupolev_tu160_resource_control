package utils;

import data.TextConstants;
import javafx.scene.control.TextFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.UnaryOperator;

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

    public static boolean checkAdditionalOperating(String hours, String minutes,
                                                String rotations, String fireMain,
                                                String fireReserve, String takeOffs) {
        int addHours = Integer.parseInt(hours);
        int addMinutes = Integer.parseInt(minutes);
        int addRotations;
        try {
            addRotations = Integer.parseInt(rotations);
        } catch (NumberFormatException e) {
            addRotations = 0;
        }
        int addFireMain = Integer.parseInt(fireMain);
        int addFireReserve = Integer.parseInt(fireReserve);
        int addTakeOffs = Integer.parseInt(takeOffs);
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

    public static boolean checkInputText(String ... text) {
        for (String t : text) {
            log.info("checkInputText. введено значение {}", t);
            if (StringUtils.isBlank(t)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkAircraftRegNumber(String regNumber) {
        if (regNumber.length() < 7) {
            ServiceUtils.showWarning(TextConstants.REG_NUMBER_CHECK);
            return false;
        }
        return true;
    }

}
