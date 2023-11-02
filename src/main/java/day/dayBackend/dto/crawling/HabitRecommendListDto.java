package day.dayBackend.dto.crawling;

import day.dayBackend.domain.crawling.RecommendedHabit;
import lombok.Getter;

@Getter
public class HabitRecommendListDto {

    private String habitSubject;
    private String habitName;

    public static HabitRecommendListDto fromEntity(RecommendedHabit recommendedHabit) {
        HabitRecommendListDto dto = new HabitRecommendListDto();
        dto.habitSubject = recommendedHabit.getHabitSubject();
        dto.habitName = recommendedHabit.getHabitName();
        return dto;
    }

}
