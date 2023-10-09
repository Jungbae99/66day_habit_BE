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

    @Query("SELECT hr FROM HabitRecord hr WHERE hr.habit.id = :habitId AND hr.habit.deletedAt IS NULL")
    List<HabitRecord> getHabitRecords(@Param("habitId") Long habitId);

}

