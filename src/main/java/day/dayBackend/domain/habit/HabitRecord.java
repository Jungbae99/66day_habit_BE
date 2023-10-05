package day.dayBackend.domain.habit;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int dayNumber;
    private int achievementRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    public HabitRecord(int dayNumber, int achievementRate) {
        this.dayNumber = dayNumber;
        this.achievementRate = achievementRate;
    }
}