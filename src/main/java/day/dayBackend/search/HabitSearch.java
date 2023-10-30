package day.dayBackend.search;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class HabitSearch {

    //:TODO 확장을 위함 -> 생성자에서 Builder 패턴으로
//    private String habitName;
//    private List<String> habitTags;

    private String keyword;
    private String sort;
}
