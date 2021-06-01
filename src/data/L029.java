package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import data.enums.ComponentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class L029 extends Component {

    private int countOfLandings;
    private int startsOnMainChannel;
    private int startsOnReserveChannel;
    private int flightOperatingTime;

    public L029(String attachedToAircraft, boolean isUnmounted,
                ComponentType type, BigDecimal number,
                int countOfLandings, int startsOnMainChannel,
                int startsOnReserveChannel, int flightOperatingTime) {
        super(attachedToAircraft, isUnmounted, type, number);
        this.countOfLandings = countOfLandings;
        this.startsOnMainChannel = startsOnMainChannel;
        this.startsOnReserveChannel = startsOnReserveChannel;
        this.flightOperatingTime = flightOperatingTime;
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
        return super.getNumber().equals(l029.getNumber())
                && countOfLandings == l029.countOfLandings
                && startsOnMainChannel == l029.startsOnMainChannel
                && startsOnReserveChannel == l029.startsOnReserveChannel
                && flightOperatingTime == l029.flightOperatingTime
                && super.isUnmounted() == l029.isUnmounted()
                && super.getType() == l029.getType()
                && Objects.equals(super.getAttachedToAircraft(), l029.getAttachedToAircraft());
    }
}
