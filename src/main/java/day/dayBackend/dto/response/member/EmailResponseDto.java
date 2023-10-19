package day.dayBackend.dto.response.member;

import day.dayBackend.domain.Member;
import lombok.Getter;

@Getter
public class EmailResponseDto {

    private String email;

    public static EmailResponseDto fromEntity(Member member) {
        EmailResponseDto dto = new EmailResponseDto();
        dto.email = member.getEmail();
        return dto;
    }
}
