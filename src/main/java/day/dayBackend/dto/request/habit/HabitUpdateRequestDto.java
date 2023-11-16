package day.dayBackend.dto.request.habit;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
public class HabitUpdateRequestDto {

    private JsonNullable<String> backgroundColor = JsonNullable.undefined();

    private JsonNullable<String> fontColor = JsonNullable.undefined();

    private JsonNullable<String> habitName = JsonNullable.undefined();

    private JsonNullable<String> habitVisibility = JsonNullable.undefined();

    private JsonNullable<List<String>> habitTag = JsonNullable.undefined();

}
