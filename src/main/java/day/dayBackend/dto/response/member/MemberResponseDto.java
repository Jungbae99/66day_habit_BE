package day.dayBackend.dto.response.member;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.Habit;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberResponseDto {

    private String username;
    private String introduction;
    private List<Habit> habitList;
    private String profileImage;

    public static MemberResponseDto fromEntity(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.username = member.getUsername();
        dto.introduction = member.getIntroduction();
        dto.habitList = member.getHabitList();
        dto.profileImage = member.getProfileImage().getUrl();
        return dto;
    }


}
