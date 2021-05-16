package data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import data.enums.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "MKU", value = MPU_MKU.class),
        @JsonSubTypes.Type(name = "MPU", value = MPU_MKU.class)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Component {
    private String attachedToAircraft;
    private boolean isUnmounted;
    private ComponentType type;
    private int number;
}
