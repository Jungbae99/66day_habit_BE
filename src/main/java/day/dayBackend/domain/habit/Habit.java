package day.dayBackend.domain.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import day.dayBackend.domain.HabitLikes;
import day.dayBackend.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Where(clause = "deleted_at is null")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Habit extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    private String habitName;

    @Enumerated(EnumType.STRING)
    private BackgroundColor backgroundColor;

    @Enumerated(EnumType.STRING)
    private FontColor fontColor;

    @Enumerated(EnumType.STRING)
    private HabitVisibility habitVisibility;

    @ElementCollection
    @CollectionTable(name = "habit_tags", joinColumns = @JoinColumn(name = "habit_id"))
    @Column(name = "tag_name")
    private List<String> habitTags = new ArrayList<>();

    private int progress; // 1 ~ 66 까지 얼마나 진행했는지에 대한 데이터

    @OneToMany(mappedBy = "habit" )
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @OneToMany(mappedBy = "habit", orphanRemoval = true)
    private List<HabitLikes> likes = new ArrayList<>();

    @Builder
    Habit(Member member, String habitName, BackgroundColor backgroundColor, FontColor fontColor, HabitVisibility habitVisibility, List<String> habitTag) {
        this.member = member;
        this.habitName = habitName;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.habitVisibility = habitVisibility;
        this.progress = 0;
        if (habitTag != null) {
            this.habitTags.addAll(habitTag);
        }
    }

    public void updateHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void updateFontColor(String fontColor) {
        this.fontColor = FontColor.valueOf(fontColor);
    }

    public void updateBackgroundColor(String backgroundColor) {
        this.backgroundColor = BackgroundColor.valueOf(backgroundColor);
    }

    public void updateHabitVisibility(String visibility) {
        this.habitVisibility = HabitVisibility.valueOf(visibility);
    }

    public void updateHabitTag(List<String> habitTags) {
        this.habitTags.clear();
        this.habitTags.addAll(habitTags);
    }

    public void updateProgress() {
        int totalRecords = habitRecords.size();
        this.progress = (totalRecords * 100) / 66;
    }

    public void delete() {
        habitTags.clear();
        super.delete();
    }
}
