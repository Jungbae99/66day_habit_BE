package day.dayBackend.dto.request.habit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HabitRecordRequestDto {

    @NotNull
    private int dayNumber;

    @NotNull
    private int achievementRate;

}
