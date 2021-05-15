package data;

import data.enums.Rank;
import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Engineer {

    @NonNull
    private Rank rank;
    @NonNull
    private String fullName;
    private ArrayList<Aircraft> attachedAircrafts;

    @Override
    public String toString() {
        return rank.getDescription() + " " + fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engineer engineer = (Engineer) o;
        return rank == engineer.rank && fullName.equals(engineer.fullName);
    }
}
