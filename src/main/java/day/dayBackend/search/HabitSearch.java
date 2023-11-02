package day.dayBackend.search;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class HabitSearch {

    private String keyword;
    private String sort;
}
