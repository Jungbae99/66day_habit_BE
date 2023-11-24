package day.dayBackend.dto.request.habit;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class HabitRecordUpdateRequestDto {

    @NotBlank(message = "{validation.NotBlank}")
    private int achievementRate;

}
