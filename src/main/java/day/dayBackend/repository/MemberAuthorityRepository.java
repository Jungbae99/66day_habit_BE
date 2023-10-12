package day.dayBackend.repository;

import day.dayBackend.domain.authority.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {

    List<MemberAuthority> findByMemberId(Long memberId);

}
