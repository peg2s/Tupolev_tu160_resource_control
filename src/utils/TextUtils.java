package utils;

import javafx.scene.control.TextFormatter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.UnaryOperator;

public class TextUtils {

    public static TextFormatter<Integer> createFormatter() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(integerFilter);
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
