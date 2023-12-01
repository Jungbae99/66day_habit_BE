package day.dayBackend.repository;

import day.dayBackend.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepositoryCustom {

    Optional<Member> findBySearchAndDeletedAtNull(String search);
}
