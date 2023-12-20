package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.HabitRecord;
import lombok.Getter;

import java.util.List;

@Getter
public class AchievementRateDto {

    private int thirty;
    private int fifty;
    private int hundred;

    public static AchievementRateDto fromEntity(Habit habit) {
        AchievementRateDto dto = new AchievementRateDto();
        List<HabitRecord> habitRecords = habit.getHabitRecords();
        int achievement20 = 0;
        int achievement50 = 0;
        int achievement100 = 0;

        for (HabitRecord habitRecord : habitRecords) {
            if (habitRecord.getAchievementRate() == 20) {
                achievement20++;
            } else if (habitRecord.getAchievementRate() == 50) {
                achievement50++;
            } else {
                achievement100++;
            }
        }
        if (habitRecords.size() != 0) {
            dto.thirty = (int)((achievement20 / (double)habitRecords.size()) * 100);
            dto.fifty = (int)((achievement50 / (double)habitRecords.size()) * 100);
            dto.hundred = (int)((achievement100 / (double)habitRecords.size()) * 100);
        }
        return dto;
    }
}
