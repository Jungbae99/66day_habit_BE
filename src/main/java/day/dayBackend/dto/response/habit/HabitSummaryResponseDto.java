package day.dayBackend.dto.response.habit;

import day.dayBackend.domain.habit.Habit;
import lombok.Getter;

import java.util.List;

@Getter
public class HabitSummaryResponseDto {

    private Long memberId;
    private String username;
    private String profileImage;
    private String backgroundImage;
    private Long habitId;
    private String habitName;
    private String backgroundColor;
    private String fontColor;
    private Integer progress;
    private String habitVisibility;
    private List<String> habitTags;

    public static HabitSummaryResponseDto fromEntity(Habit entity) {
        HabitSummaryResponseDto dto = new HabitSummaryResponseDto();
        dto.memberId = entity.getMember().getId();
        dto.username = entity.getMember().getUsername();
        dto.profileImage = entity.getMember().getProfileImage().getUrl();
        dto.backgroundImage = entity.getMember().getBackgroundImage().getUrl();
        dto.habitId = entity.getId();
        dto.habitName = entity.getHabitName();
        dto.backgroundColor = entity.getBackgroundColor().toString();
        dto.fontColor = entity.getFontColor().toString();
        dto.progress = entity.getProgress();
        dto.habitVisibility = String.valueOf(entity.getHabitVisibility());
        dto.habitTags = entity.getHabitTags();
        return dto;
    }
}
