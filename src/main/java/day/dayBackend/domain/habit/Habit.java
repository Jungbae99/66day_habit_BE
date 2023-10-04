package day.dayBackend.domain.habit;

import day.dayBackend.domain.Likes;
import day.dayBackend.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Habit extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String habitName;

    @Enumerated(EnumType.STRING)
    private BackGroundColor backGroundColor;

    @Enumerated(EnumType.STRING)
    private FontColor fontColor;

    @Enumerated(EnumType.STRING)
    private HabitVisibility habitVisibility;

    @OneToMany(mappedBy = "habit")
    private List<HabitTag> habitTag = new ArrayList<>();

    private int progress;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @OneToMany(mappedBy = "habit")
    private List<Likes> likes = new ArrayList<>();

    @Builder
    Habit(Member member, String habitName, BackGroundColor backGroundColor, FontColor fontColor, HabitVisibility habitVisibility, List<HabitTag> habitTag) {
        this.member = member;
        this.habitName = habitName;
        this.backGroundColor = backGroundColor;
        this.fontColor = fontColor;
        this.habitVisibility = habitVisibility;
        this.progress = 0;
        if (habitTag != null) {
            this.habitTag.addAll(habitTag);
        }
    }
}
