package day.dayBackend.repository;

import day.dayBackend.domain.habit.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    Optional<Habit> findByIdAndDeletedAtNull(Long id);
    Optional<Habit> findByHabitNameAndDeletedAtNull(String habitName);

}
