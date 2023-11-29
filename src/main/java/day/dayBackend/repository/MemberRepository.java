package day.dayBackend.repository;

import day.dayBackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmailAndDeletedAtNull(String email);

    Optional<Member> findByIdAndDeletedAtNull(Long memberId);

    Optional<Member> findByUsernameAndDeletedAtNull(String username);

    @Query(value = "select m from Member m " +
            "left join fetch m.habitList mh " +
            "left join fetch m.profileImage " +
            "left join fetch m.backgroundImage " +
            "where m.id = :memberId " +
            "and m.deletedAt is null ")
    Optional<Member> findByIdWithHabit(@Param("memberId") Long memberId);

    @Query(value = "select m from Member m " +
            "join fetch m.profileImage mp " +
            "join fetch m.backgroundImage mb " +
            "where m.id = :memberId " +
            "and m.deletedAt is null")
    Optional<Member> findByIdWithUpload(@Param("memberId") Long memberId);
}
