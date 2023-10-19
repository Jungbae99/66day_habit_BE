package day.dayBackend.dto.response.member;

import day.dayBackend.domain.Member;
import lombok.Getter;

@Getter
public class MemberDetailResponseDto {

    private String username;
    private String profileImage;
    private String backgroundImage;
    private String introduction;

    public static MemberDetailResponseDto fromEntity(Member member) {
        MemberDetailResponseDto dto = new MemberDetailResponseDto();
        dto.username = member.getUsername();
        dto.profileImage = member.getProfileImage().getUrl();
        dto.backgroundImage = member.getBackgroundImage().getUrl();
        dto.introduction = member.getIntroduction();
        return dto;
    }
}
