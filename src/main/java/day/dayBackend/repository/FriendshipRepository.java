package day.dayBackend.repository;

import day.dayBackend.domain.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


    Optional<Friendship> findByFollowerIdAndFollowingIdAndDeletedAtNull(Long followerId, Long followingId);

    Page<Friendship> findFriendshipByFollowerIdAndDeletedAtNull(Long memberId, Pageable pageable);

    Optional<Friendship> findFriendshipByFollowingIdAndDeletedAtNull(Long followerId);
}
