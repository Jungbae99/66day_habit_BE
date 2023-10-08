package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.dto.request.habit.HabitUpdateRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class HabitUpdateResponseDto {

    private String backgroundColor;
    private String fontColor;
    private String habitName;
    private String habitVisibility;
    private List<String> habitTag;

    public static HabitUpdateResponseDto fromEntity(Habit habit) {
        HabitUpdateResponseDto dto = new HabitUpdateResponseDto();
        dto.backgroundColor = String.valueOf(habit.getBackgroundColor());
        dto.fontColor = String.valueOf(habit.getFontColor());
        dto.habitName = habit.getHabitName();
        dto.habitVisibility = String.valueOf(habit.getHabitVisibility());
        dto.habitTag = habit.getHabitTags();
        return dto;
    }

}
