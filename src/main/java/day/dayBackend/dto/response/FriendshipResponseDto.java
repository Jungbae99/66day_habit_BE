package day.dayBackend.dto.response;

import day.dayBackend.domain.Friendship;
import lombok.Getter;

@Getter
public class FriendshipResponseDto {

    private Long memberId;
    private String username;

    public static FriendshipResponseDto fromEntity(Friendship friendship) {
        FriendshipResponseDto dto = new FriendshipResponseDto();
        dto.memberId = friendship.getFollowing().getId();
        dto.username = friendship.getFollowing().getUsername();
        return dto;
    }
}
