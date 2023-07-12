package reactive.example;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamPlan {
    public TeamPlan(int idx) {
        this.idx = idx;
    }

    private int idx;
}
