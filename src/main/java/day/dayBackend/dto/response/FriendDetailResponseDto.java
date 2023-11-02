package day.dayBackend.dto.response;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.Habit;
import day.dayBackend.dto.response.habit.HabitSummaryResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FriendDetailResponseDto {

    private Long friendId;
    private String username;
    private String introduction;
    private String profileImage;
    private String backgroundImage;
    private Integer isFriend;
    private List<HabitSummaryResponseDto> friendHabitList; // :TODO

    public static FriendDetailResponseDto fromFriend(Member friend, List<Habit> habitList, Integer friendCheck) {
        FriendDetailResponseDto dto = new FriendDetailResponseDto();
        dto.friendId = friend.getId();
        dto.username = friend.getUsername();
        dto.introduction = friend.getIntroduction();
        dto.profileImage = friend.getProfileImage().getUrl();
        dto.backgroundImage = friend.getBackgroundImage().getUrl();
        dto.isFriend = friendCheck == 1 ? 1 : 0;
        dto.friendHabitList = habitList.stream()
                .map(habit -> HabitSummaryResponseDto.fromEntity(habit))
                .collect(Collectors.toList());

        return dto;
    }
}
