package day.dayBackend.repository;

import day.dayBackend.domain.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // follower id 로  친구정보를 찾기
    Optional<Friendship> findByFollowerIdAndFollowingIdAndDeletedAtNull(Long followerId, Long followingId);

    // 내 친구 조회
    Page<Friendship> findFriendshipByFollowerIdAndDeletedAtNull(Long memberId, Pageable pageable);

    // 내 친구를 member PK로 조회
    Optional<Friendship> findFriendshipByFollowingIdAndDeletedAtNull(Long friendId);
}
