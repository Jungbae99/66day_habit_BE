package day.dayBackend.dto.response;

import day.dayBackend.domain.Friendship;
import lombok.Getter;

@Getter
public class FriendshipResponseDto {

    private Long memberId;
    private String username;
    private String profileImage;

    public static FriendshipResponseDto fromEntity(Friendship friendship) {
        FriendshipResponseDto dto = new FriendshipResponseDto();
        dto.memberId = friendship.getFollowing().getId();
        dto.username = friendship.getFollowing().getUsername();
        dto.profileImage = friendship.getFollowing().getProfileImage().getUrl();
        return dto;
    }
}
