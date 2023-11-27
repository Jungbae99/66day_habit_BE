package day.dayBackend.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {

    private String type;

    @NotNull(message = "{validation.NotNull}")
    private Long senderId;

    @NotNull(message = "{validation.NotNull}")
    private Long receiverId;

    private String content;

}
