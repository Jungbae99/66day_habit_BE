package day.dayBackend.dto.request.habit;

import day.dayBackend.domain.habit.BackgroundColor;
import day.dayBackend.util.validation.enums.EnumValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class HabitCreateRequestDto {

    @EnumValid(enumClass = BackgroundColor.class)
    @NotBlank(message = "{validation.NotBlank}")
    private String backgroundColor;

    @NotBlank(message = "{validation.NotBlank}")
    private String fontColor;

    @NotBlank(message = "{validation.NotBlank}")
    private String habitName;

    @NotBlank(message = "{validation.NotBlank}")
    private String habitVisibility;

    private List<String> habitTag;

}
