package day.dayBackend.service;

import day.dayBackend.domain.Friendship;
import day.dayBackend.domain.Member;
import day.dayBackend.dto.response.FriendshipResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.FriendshipRepository;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FriendshipService {

    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;

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
    public Long deleteFriend(Long memberId, Long followId) {

        if (memberId.equals(followId)) {
            throw new IllegalArgumentException("자신을 팔로우 할 수 없습니다.");
        }
        memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Friendship friendship = friendshipRepository.findFriendshipByFollowingIdAndDeletedAtNull(followId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 친구입니다."));

        friendship.delete();
        return friendship.getId();
    }
}

