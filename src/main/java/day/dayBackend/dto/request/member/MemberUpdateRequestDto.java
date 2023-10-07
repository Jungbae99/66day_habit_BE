package day.dayBackend.dto.request.member;

import day.dayBackend.domain.Upload;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MemberUpdateRequestDto {

    private JsonNullable<String> username = JsonNullable.undefined();

    private JsonNullable<String> introduction = JsonNullable.undefined();

//:TODO    private JsonNullable<MultipartFile> profileImage = JsonNullable.undefined();

}
