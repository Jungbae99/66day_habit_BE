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
public class Likes extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    public static Likes createLikes(Member member, Habit habit) {
        Likes likes = new Likes();
        likes.member = member;
        likes.habit = habit;
        return likes;
    }

    public void delete() {
        super.delete();
    }
}
