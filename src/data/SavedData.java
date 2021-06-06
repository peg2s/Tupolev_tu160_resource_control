package data;

import io.reader.ReadFile;
import io.writer.WriteFile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SavedData {
    public static List<Engineer> engineers = ReadFile.readInfo(Engineer.class, "data.Engineer","ae");
    public static List<Aircraft> aircraft = ReadFile.readInfo(Aircraft.class, "data.Aircraft","ae");
    public static List<Component> components = ReadFile.readInfo(Component.class, "data.Component","ae");

    public static void saveCurrentStateData() {
        WriteFile.saveInfo(Engineer.class, SavedData.engineers, "ae");
        WriteFile.saveInfo(Aircraft.class, SavedData.aircraft, "ae");
        WriteFile.saveInfo(Component.class, SavedData.components, "ae");
    }

    public static void readDataFromSave() {
        engineers = ReadFile.readInfo(Engineer.class, "data.Engineer","ae");
        aircraft = ReadFile.readInfo(Aircraft.class, "data.Aircraft","ae");
        components = ReadFile.readInfo(Component.class, "data.Component","ae");
    }


    public static Engineer getEngineerFromList(String engineerRankAndName) {
        log.info("getEngineerFromList(), engineerRankAndName {}", engineerRankAndName);
        for (Engineer e : engineers) {
            if (e.toString().equals(engineerRankAndName)) {
                return e;
            }
        }
        return null;
    }

    public static int getComponentId(Component component) {
        log.info("getComponentId(), component {}", component);
        for (int i = 0; i<components.size(); i++) {
            if (component.equals(components.get(i))) {
                return i;
            }
        }
        return 0;
    }

    public static Aircraft getAircraft(String selectedAircraft) {
        log.info("getAircraft(), selectedAircraft {}", selectedAircraft);
        return aircraft.stream().filter(a -> a.toString().equals(selectedAircraft)).findAny().orElse(null);
    }
}
