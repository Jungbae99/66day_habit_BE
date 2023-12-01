package day.dayBackend.repository.recommend;


import day.dayBackend.domain.recommend.RecommendedQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRecommendRepository extends JpaRepository<RecommendedQuote, Long> {

    @Query(value = "select rq from RecommendedQuote rq order by rand() limit 1")
    Optional<RecommendedQuote> findRandomQuote();

}
