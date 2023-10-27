package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.HabitRecord;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
public class HabitDetailResponseDto {

    private int progress;
    private AchievementRateDto achievementRates;
    private boolean isTodayChecked;

    public static HabitDetailResponseDto fromEntity(Habit habit) {
        HabitDetailResponseDto dto = new HabitDetailResponseDto();
        dto.progress = habit.getProgress();
        dto.achievementRates = AchievementRateDto.fromEntity(habit);
        dto.isTodayChecked = dto.hasCheckToday(habit.getHabitRecords()) ? true : false;
        return dto;
    }

    /**
     * 오늘 기록을 추가했는지 여부를 판단
     */
    private boolean hasCheckToday(List<HabitRecord> habitRecords) {
        LocalDateTime time = LocalDateTime.now();
        for (HabitRecord habitRecord : habitRecords) {
            if (time.getDayOfYear() == habitRecord.getCreatedAt().getDayOfYear()) {
                return true;
            }
        }
        return false;
    }
}