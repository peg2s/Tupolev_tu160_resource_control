package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import data.enums.ComponentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class L029 extends Component {

    public L029(String attachedToAircraft, boolean isUnmounted,
                ComponentType type, BigDecimal number,
                int countOfLandings, int startsOnMainChannel,
                int startsOnReserveChannel, int flightOperatingTime) {
        super(UUID.randomUUID(), attachedToAircraft, isUnmounted,
                type, number, countOfLandings, startsOnMainChannel,
                startsOnReserveChannel, flightOperatingTime);
    }

    @JsonCreator
    public L029() {
        super();
    }

    @Override
    public String toString() {
        return "изделие: " + super.getType().getName() + ", серийный номер: " + super.getNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        L029 l029 = (L029) o;
        return getUuid().equals(l029.getUuid());
    }
}
