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
    @NotBlank(message = "{validation.NotBlank}")
    private String email;

//    @NotNull(message = "{validation.NotNull}")
//    @Size(min = 8, max = 24, message = "{validation.Size}")
    private String password;

//    @NotNull(message = "{validation.NotNull}")
    private String username;

    private String introduction;

}