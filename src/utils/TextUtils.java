package utils;

import javafx.scene.control.TextFormatter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.UnaryOperator;

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

    public static boolean checkInputText(String ... text) {
        for (String t : text) {
            if (StringUtils.isBlank(t)) {
                return false;
            }
        }
        return true;
    }

}
