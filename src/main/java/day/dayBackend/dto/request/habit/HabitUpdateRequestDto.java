package day.dayBackend.dto.request.habit;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
public class HabitUpdateRequestDto {

    private JsonNullable<String> backgroundColor;

    private JsonNullable<String> fontColor;

    private JsonNullable<String> habitName;

    private JsonNullable<String> habitVisibility;

    private JsonNullable<List<String>> habitTag;

}
