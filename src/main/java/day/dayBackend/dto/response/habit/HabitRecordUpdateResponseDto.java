package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.HabitRecord;
import lombok.Getter;

@Getter
public class HabitRecordUpdateResponseDto {

    private int dayNumber;
    private int achievementRate;

    public static HabitRecordUpdateResponseDto fromEntity(HabitRecord habitRecord) {
        HabitRecordUpdateResponseDto dto = new HabitRecordUpdateResponseDto();
        dto.dayNumber = habitRecord.getDayNumber();
        dto.achievementRate = habitRecord.getAchievementRate();
        return dto;
    }
}
