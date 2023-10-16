package day.dayBackend.domain;

import day.dayBackend.domain.habit.BaseAuditingListener;
import day.dayBackend.domain.habit.Habit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitLikes extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    public static HabitLikes createLikes(Member member, Habit habit) {
        HabitLikes habitLikes = new HabitLikes();
        habitLikes.member = member;
        habitLikes.habit = habit;
        return habitLikes;
    }

    public void delete() {
        super.delete();
    }
}
