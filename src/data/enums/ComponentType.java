package data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ComponentType {

    MKU("9А-829К2", 2),
    MPU("9А-829К3", 2),
    L029("Л-029", 3);

    private final String name;
    private final int maxCountOnAircraft;

    @Override
    public String toString() {
        return this.getName();
    }
}
