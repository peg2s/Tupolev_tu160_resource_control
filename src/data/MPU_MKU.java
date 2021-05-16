package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import data.enums.ComponentType;
import lombok.Data;

import java.util.Objects;

@Data
public class MPU_MKU extends Component {

    private int countOfLandings;
    private int startsOnMainChannel;
    private int startsOnReserveChannel;
    private int flightOperatingTime;
    private int rotationsCount;

    public MPU_MKU(String attachedToAircraft, boolean isUnmounted, ComponentType type, int number, int countOfLandings, int startsOnMainChannel, int startsOnReserveChannel, int flightOperatingTime, int rotationsCount) {
        super(attachedToAircraft, isUnmounted, type, number);
        this.countOfLandings = countOfLandings;
        this.startsOnMainChannel = startsOnMainChannel;
        this.startsOnReserveChannel = startsOnReserveChannel;
        this.flightOperatingTime = flightOperatingTime;
        this.rotationsCount = rotationsCount;
    }

    @JsonCreator
    public MPU_MKU() {
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
        MPU_MKU mpu_mku = (MPU_MKU) o;
        return super.getNumber() == mpu_mku.getNumber()
                && countOfLandings == mpu_mku.countOfLandings
                && startsOnMainChannel == mpu_mku.startsOnMainChannel
                && startsOnReserveChannel == mpu_mku.startsOnReserveChannel
                && flightOperatingTime == mpu_mku.flightOperatingTime
                && rotationsCount == mpu_mku.rotationsCount
                && super.isUnmounted() == mpu_mku.isUnmounted()
                && super.getType() == mpu_mku.getType()
                && Objects.equals(super.getAttachedToAircraft(), mpu_mku.getAttachedToAircraft());
    }
}
