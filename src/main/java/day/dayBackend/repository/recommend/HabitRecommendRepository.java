package day.dayBackend.repository.recommend;

import day.dayBackend.domain.recommend.RecommendedHabit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HabitRecommendRepository extends JpaRepository<RecommendedHabit, Long> {

    @Query(value = "select rh from RecommendedHabit rh order by rand() limit 10")
    Optional<List<RecommendedHabit>> findRandomHabit();

}
