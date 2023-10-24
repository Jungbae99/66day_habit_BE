package day.dayBackend.dto.response.member;


import day.dayBackend.domain.Member;
import lombok.Getter;

@Getter
public class MemberUpdateResponseDto {

    private String username;
    private String introduction;
    private String profileImage;
    private String backgroundImage;

    public static MemberUpdateResponseDto fromEntity(Member member) {
        MemberUpdateResponseDto dto = new MemberUpdateResponseDto();
        dto.username = member.getUsername();
        dto.introduction = member.getIntroduction();
        dto.profileImage = member.getProfileImage().getUrl();
        dto.backgroundImage = member.getBackgroundImage().getUrl();
        return dto;
    }
}
