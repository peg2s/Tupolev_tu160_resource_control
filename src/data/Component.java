package data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import data.enums.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "MKU", value = MPU_MKU.class),
        @JsonSubTypes.Type(name = "MPU", value = MPU_MKU.class),
        @JsonSubTypes.Type(name = "L029", value = L029.class)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Component {
    private UUID uuid;
    private String attachedToAircraft;
    private boolean isUnmounted;
    private ComponentType type;
    private BigDecimal number;
}
