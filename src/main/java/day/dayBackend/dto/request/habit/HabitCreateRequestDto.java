package day.dayBackend.dto.request.habit;

import lombok.Getter;

import java.util.List;

@Getter
public class HabitCreateRequestDto {

    private String backgroundColor;
    private String fontColor;
    private String habitName;
    private String habitVisibility;
    private List<String> habitTag;

}
