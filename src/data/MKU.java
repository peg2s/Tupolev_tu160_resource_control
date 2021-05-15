package data;

import data.enums.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MKU implements Component {

    private final ComponentType type = ComponentType.MKU;
    private int number;
    private int countOfLandings;
    private int startsOnMainChannel;
    private int startsOnReserveChannel;
    private int flightOperatingTime;
    private String attachedToAircraft;

}
