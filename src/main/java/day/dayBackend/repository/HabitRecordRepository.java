package day.dayBackend.repository;

import day.dayBackend.domain.habit.HabitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {

    @Query("SELECT hr FROM HabitRecord hr WHERE hr.habit.id = :habitId AND hr.habit.deletedAt IS NULL ORDER BY hr.dayNumber ASC")
    List<HabitRecord> getHabitRecords(@Param("habitId") Long habitId);

    @Query("SELECT hr FROM HabitRecord hr WHERE hr.habit.id = :habitId AND hr.dayNumber =:dayNumber AND hr.habit.deletedAt IS NULL")
    Optional<HabitRecord> findByHabitId(@Param("habitId") Long habitId, @Param("dayNumber") Integer dayNumber);

}