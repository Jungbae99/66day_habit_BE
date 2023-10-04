package day.dayBackend.domain;

import day.dayBackend.domain.habit.BaseAuditingListener;
import day.dayBackend.domain.habit.Habit;
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
public class Member extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private String username;

    private String introduction;

    private String profileImage;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Certified certified;

    @OneToMany(mappedBy = "member")
    private List<Habit> habitList = new ArrayList<>();

    @Builder
    Member(String email, String password, String username, String introduction) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.introduction = introduction;
        this.certified = Certified.NOT_CERTIFIED;
    }

    public void delete() {
        super.delete();
    }
}
