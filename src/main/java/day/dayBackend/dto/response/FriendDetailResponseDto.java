package day.dayBackend.dto.response;

import day.dayBackend.domain.Friendship;
import day.dayBackend.domain.Member;
import day.dayBackend.dto.response.habit.HabitSummaryResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FriendDetailResponseDto {

    private String username;
    private String introduction;
    private String profileImage;
    private String backgroundImage;
    private Long friendShipId;
    private List<HabitSummaryResponseDto> friendHabitList; // :TODO

    public static FriendDetailResponseDto fromEntity(Member friend, Member me) {
        FriendDetailResponseDto dto = new FriendDetailResponseDto();
        dto.username = friend.getUsername();
        dto.introduction = friend.getIntroduction();
        dto.profileImage = friend.getProfileImage().getUrl();
        dto.backgroundImage = friend.getBackgroundImage().getUrl();

        Friendship friendship = friend.getFollowing().stream().filter(f -> f.getFollower().equals(me))
                .findFirst().orElse(null);

        dto.friendShipId = friendship != null ? friendship.getId() : null;

        dto.friendHabitList = friend.getHabitList().stream()
                .map(habit -> HabitSummaryResponseDto.fromEntity(habit))
                .collect(Collectors.toList());

        return dto;
    }
}
