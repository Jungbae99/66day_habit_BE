package day.dayBackend.repository.crawling;

import day.dayBackend.domain.crawling.RecommendedHabit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HabitRecommendRepository extends JpaRepository<RecommendedHabit, Long> { }
