package day.dayBackend.repository;

import day.dayBackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndDeletedAtNull(String email);

    Optional<Member> findByIdAndDeletedAtNull(Long memberId);

    Optional<Member> findByUsernameAndDeletedAtNull(String username);

}
