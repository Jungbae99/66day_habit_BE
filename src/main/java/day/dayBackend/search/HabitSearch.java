package day.dayBackend.search;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class HabitSearch {

    private String keyword1;
    private String keyword2;
    private String sort;
}
