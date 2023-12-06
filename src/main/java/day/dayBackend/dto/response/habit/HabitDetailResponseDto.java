package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.HabitRecord;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
public class HabitDetailResponseDto {

    private String habitName;
    private int progress;
    private AchievementRateDto achievementRates;
    private boolean isTodayChecked;
    private int inspireDay;

    public static HabitDetailResponseDto of(Habit habit, Boolean isTodayChecked, int inspireDay) {
        HabitDetailResponseDto dto = new HabitDetailResponseDto();
        dto.habitName = habit.getHabitName();
        dto.progress = habit.getProgress();
        dto.achievementRates = AchievementRateDto.fromEntity(habit);
        dto.isTodayChecked = isTodayChecked;
        dto.inspireDay = inspireDay;
        return dto;
    }

}