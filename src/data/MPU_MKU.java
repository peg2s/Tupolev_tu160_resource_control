package data;

import com.fasterxml.jackson.annotation.JsonCreator;
import data.enums.ComponentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MPU_MKU extends Component {

    private int rotationsCount;

    public MPU_MKU(String attachedToAircraft, boolean isUnmounted, ComponentType type, BigDecimal number,
                   int countOfLandings, int startsOnMainChannel, int startsOnReserveChannel,
                   int flightOperatingTime, int rotationsCount) {
        super(UUID.randomUUID(), attachedToAircraft, isUnmounted,
                type, number, countOfLandings, startsOnMainChannel,
                startsOnReserveChannel, flightOperatingTime);
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
        return getUuid().equals(mpu_mku.getUuid());
    }
}
