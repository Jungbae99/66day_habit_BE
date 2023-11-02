package day.dayBackend.repository.crawling;


import day.dayBackend.domain.crawling.RecommendedQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRecommendRepository extends JpaRepository<RecommendedQuote, Long> {
}
