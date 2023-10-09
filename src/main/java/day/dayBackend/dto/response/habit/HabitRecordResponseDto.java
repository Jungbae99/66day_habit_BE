package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.HabitRecord;
import lombok.Getter;

@Getter
public class HabitRecordResponseDto {

    private int dayNumber;
    private int achievementRate;

    public static HabitRecordResponseDto fromEntity(HabitRecord habitRecord) {
        HabitRecordResponseDto dto = new HabitRecordResponseDto();
        dto.dayNumber = habitRecord.getDayNumber();
        dto.achievementRate = habitRecord.getAchievementRate();
        return dto;
    }
}
