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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Upload profileImage;

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

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateProfileImage(Upload profileImage) {
        this.profileImage = profileImage;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void delete() {
        super.delete();
    }
}
