package day.dayBackend.service;

import day.dayBackend.domain.Friendship;
import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.Habit;
import day.dayBackend.dto.response.FriendDetailResponseDto;
import day.dayBackend.dto.response.FriendshipResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.FriendshipRepository;
import day.dayBackend.repository.HabitRepository;
import day.dayBackend.repository.MemberRepository;
import day.dayBackend.repository.custom.MemberRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FriendshipService {

    private final MemberRepository memberRepository;
    private final MemberRepositoryImpl memberRepositoryImpl;
    private final FriendshipRepository friendshipRepository;
    private final HabitRepository habitRepository;

    /**
     * 친구 추가하기
     */
    @Transactional
    public Long followFriend(Long memberId, Long followId) {

        if (memberId.equals(followId)) {
            throw new IllegalArgumentException("자신을 팔로우 할 수 없습니다.");
        }

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Member followedMember = memberRepository.findByIdAndDeletedAtNull(followId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));

        if (friendshipRepository.findByFollowerIdAndFollowingIdAndDeletedAtNull(member.getId(), followedMember.getId()).isPresent()) {
            throw new DuplicateKeyException("이미 팔로우 상태 입니다.");
        }
        Friendship friendship = Friendship.builder()
                .follower(member)
                .following(followedMember)
                .build();

        friendshipRepository.save(friendship);
        return friendship.getId();
    }

    /**
     * 친구 상세정보 조회
     */
    public FriendDetailResponseDto getFriendDetailById(Long memberId, Long friendId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Member friend = memberRepository.findByIdAndDeletedAtNull(friendId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Optional<List<Habit>> friendHabitList;
        boolean friendCheck = false;

        if (friendshipRepository.findByFollowerIdAndFollowingIdAndDeletedAtNull(memberId, friendId).isPresent()) {
            friendHabitList = habitRepository.findHabitNotPrivate(friendId);
            // 친구일 때 true;
            friendCheck = true;
        } else {
            friendHabitList = habitRepository.findPublicHabits(friendId);
        }

        return FriendDetailResponseDto.fromFriend(friend, friendHabitList.get(), friendCheck);
    }

    /**
     * 친구 검색
     */
    public FriendDetailResponseDto getFriendDetailBySearch(Long memberId, String search) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Member friend = memberRepositoryImpl.findBySearchAndDeletedAtNull(search)
                .orElseThrow(() -> new NotFoundException("해당하는 회원을 찾을 수 없습니다."));;

        Optional<List<Habit>> friendHabitList;

        boolean friendCheck = false;
        // 친구일 때
        if (friendshipRepository.findByFollowerIdAndFollowingIdAndDeletedAtNull(memberId, friend.getId()).isPresent()) {
            friendHabitList = habitRepository.findHabitNotPrivate(friend.getId());
            friendCheck = true;
        // 친구가 아닐 때
        } else {
            friendHabitList = habitRepository.findPublicHabits(friend.getId());
        }

        return FriendDetailResponseDto.fromFriend(friend, friendHabitList.get(), friendCheck);
    }
    
    /**
     * 내가 팔로우하는 친구 조회
     */
    public List<FriendshipResponseDto> getFollowList(Long memberId, Pageable pageable) {
        Page<Friendship> followListByMemberId = friendshipRepository.findFriendshipByFollowerIdAndDeletedAtNull(memberId, pageable);
        List<FriendshipResponseDto> followList = followListByMemberId.stream()
                .map(FriendshipResponseDto::fromEntity)
                .collect(Collectors.toList());

        return followList;
    }

    /**
     * 친구 삭제
     */
    @Transactional
    public Long deleteFriend(Long memberId, Long friendId) {

        if (memberId.equals(friendId)) {
            throw new IllegalArgumentException("자신을 팔로우 할 수 없습니다.");
        }
        memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Friendship friendship = friendshipRepository.findFriendshipByFollowingIdAndDeletedAtNull(friendId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 친구입니다."));

        friendship.delete();
        return friendship.getId();
    }
}

