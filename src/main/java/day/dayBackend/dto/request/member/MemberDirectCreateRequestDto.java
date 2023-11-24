package day.dayBackend.dto.request.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberDirectCreateRequestDto {

//  :TODO
    @Email(message = "{validation.Pattern.email}")
    private String email;

    @NotBlank(message = "{validation.NotBlank}")
    @Size(min = 8, max = 24, message = "{validation.Size}")
    private String password;

    @NotBlank(message = "{validation.NotBlank}")
    private String username;

    private String introduction;

}