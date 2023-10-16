package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.HabitLikes;
import lombok.Getter;

@Getter
public class HabitLikesResponseDto {

    private Long habitLikesId;
    private Long memberId;
    private HabitSummaryResponseDto habit;

    public static HabitLikesResponseDto fromEntity(HabitLikes entity) {
        HabitLikesResponseDto dto = new HabitLikesResponseDto();
        dto.habitLikesId = entity.getId();
        dto.memberId = entity.getMember().getId();
        dto.habit = HabitSummaryResponseDto.fromEntity(entity.getHabit());
        return dto;
    }

}
