package day.dayBackend.domain.habit;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int dayNumber;
    private int achievementRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Builder
    public HabitRecord(int dayNumber, int achievementRate, Habit habit) {
        this.dayNumber = dayNumber;
        this.achievementRate = achievementRate;
        this.habit = habit;
    }
}