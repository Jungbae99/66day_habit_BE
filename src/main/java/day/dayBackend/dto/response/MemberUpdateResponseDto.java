package day.dayBackend.dto.response;


import day.dayBackend.domain.Member;
import lombok.Getter;

@Getter
public class MemberUpdateResponseDto {

    private String username;
    private String introduction;
//    private String profileImage;

    public static MemberUpdateResponseDto fromEntity(Member member) {
        MemberUpdateResponseDto dto = new MemberUpdateResponseDto();
        dto.username = member.getUsername();
        dto.introduction = member.getIntroduction();
        //TODO: url
//        dto.profileImage = String.valueOf(member.getProfileImage());
        return dto;
    }


}
