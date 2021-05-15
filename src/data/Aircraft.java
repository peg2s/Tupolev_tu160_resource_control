package data;

import lombok.*;

import java.util.ArrayList;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Aircraft {
    @NonNull
    private String regNumber;
    @NonNull
    private String sideNumber;
    @NonNull
    private String name;
    private ArrayList<Component> components;
    @NonNull
    private String engineer;

    @Override
    public String toString() {
        return "Самолёт " + this.getSideNumber() + ", " + "\"" + this.getName() + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return regNumber.equals(aircraft.regNumber) && sideNumber.equals(aircraft.sideNumber) && name.equals(aircraft.name);
    }
}
