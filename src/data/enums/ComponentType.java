package data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ComponentType {

    MKU("9А-829К2"),
    MPU("9А-829К3"),
    L029("Л-029");

    private final String name;

    @Override
    public String toString() {
        return this.getName();
    }
}
