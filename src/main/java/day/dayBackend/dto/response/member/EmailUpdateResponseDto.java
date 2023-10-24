package day.dayBackend.dto.response.member;

import day.dayBackend.domain.Member;
import lombok.Getter;

@Getter
public class EmailUpdateResponseDto {

    private String newEmail;

    public static EmailUpdateResponseDto fromEntity(Member member) {
        EmailUpdateResponseDto dto = new EmailUpdateResponseDto();
        dto.newEmail = member.getEmail();
        return dto;
    }

}
