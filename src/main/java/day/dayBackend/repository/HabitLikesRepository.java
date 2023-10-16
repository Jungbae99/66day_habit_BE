package day.dayBackend.repository;

import day.dayBackend.domain.HabitLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitLikesRepository extends JpaRepository<HabitLikes, Long> {

    Optional<HabitLikes> findByMemberIdAndHabitLikesIdAndDeletedAtNull(Long memberId, Long habitLikesId);
    Optional<HabitLikes> findByIdAndDeletedAtNull(Long habitLikesId);

    @Query(value = "select hl from HabitLikes hl " +
            "join hl.member hlm " +
            "where hlm.id = :memberId and hl.deletedAt is null")
    Page<HabitLikes> findHabitLikesByMemberId(@Param("memberId") Long memberId, Pageable pageable);


}
