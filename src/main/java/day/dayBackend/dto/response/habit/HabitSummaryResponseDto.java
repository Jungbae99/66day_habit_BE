package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.Habit;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HabitSummaryResponseDto {

    private Long habitId;
    private String habitName;
    private String backgroundColor;
    private String fontColor;
    private List<String> habitTags;
    private HabitDetailResponseDto habitDetail;
    private List<HabitRecordResponseDto> habitRecord;

    public static HabitSummaryResponseDto fromEntity(Habit entity) {
        HabitSummaryResponseDto dto = new HabitSummaryResponseDto();
        dto.habitId = entity.getId();
        dto.habitName = entity.getHabitName();
        dto.backgroundColor = entity.getBackgroundColor().toString();
        dto.fontColor = entity.getFontColor().toString();
        dto.habitTags = entity.getHabitTags();
        dto.habitDetail = HabitDetailResponseDto.fromEntity(entity);
        dto.habitRecord = entity.getHabitRecords().stream().
                map(habitRecords -> HabitRecordResponseDto.fromEntity(habitRecords))
                .collect(Collectors.toList());

        return dto;
    }
}
