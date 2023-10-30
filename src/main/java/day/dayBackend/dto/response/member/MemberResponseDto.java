package day.dayBackend.dto.response.member;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.Habit;
import day.dayBackend.dto.response.habit.HabitSummaryResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberResponseDto {

    private String username;
    private String introduction;
    private List<HabitSummaryResponseDto> habitList;
    private String profileImage;
    private String backgroundImage;

    public static MemberResponseDto fromEntity(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.username = member.getUsername();
        dto.introduction = member.getIntroduction();
        dto.habitList = member.getHabitList().stream()
                .filter(habit -> habit.getDeletedAt() == null)
                .map(HabitSummaryResponseDto::fromEntity)
                .collect(Collectors.toList());
        dto.profileImage = member.getProfileImage().getUrl();
        dto.backgroundImage = member.getBackgroundImage().getUrl();
        return dto;
    }


}
