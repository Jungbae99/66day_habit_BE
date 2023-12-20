package day.dayBackend.repository;

import day.dayBackend.domain.Certified;
import day.dayBackend.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {

    @Query("select e from EmailCertification e " +
            "where e.certCode = :certCode " +
            "and e.email = :email " +
            "and e.deletedAt is NULL " +
            "order by e.id desc " +
            "limit 1")
    Optional<EmailCertification> findByCertCodeAndEmail(@Param("certCode")String certCode,
                                                        @Param("email")String email);

    @Query("select e from EmailCertification  e " +
            "where e.email = :email " +
            "and e.certified = :certified " +
            "order by e.id desc " +
            "limit 1")
    Optional<EmailCertification> findByEmailAndCertified(@Param("email") String email, @Param("certified") Certified certified);

}
