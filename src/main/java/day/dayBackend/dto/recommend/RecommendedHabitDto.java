package day.dayBackend.dto.recommend;

import day.dayBackend.domain.recommend.RecommendedHabit;
import lombok.Getter;

@Getter
public class RecommendedHabitDto {

    private String habitSubject;
    private String habitName;

    public static RecommendedHabitDto fromEntity(RecommendedHabit recommendedHabit) {
        RecommendedHabitDto dto = new RecommendedHabitDto();
        dto.habitSubject = recommendedHabit.getHabitSubject();
        dto.habitName = recommendedHabit.getHabitName();
        return dto;
    }

}
