package day.dayBackend.domain;

import day.dayBackend.domain.authority.MemberAuthority;
import day.dayBackend.domain.habit.BaseAuditingListener;
import day.dayBackend.domain.habit.Habit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private String username;

    private String introduction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Upload profileImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "background_image_id")
    private Upload backgroundImage;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Certified certified;

    @OneToMany(mappedBy = "member")
    private List<Habit> habitList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberAuthority> memberAuthorities = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private Set<Friendship> following = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    private Set<Friendship> follower = new HashSet<>();

    @Builder
    Member(String email, String password, String username, String introduction, Upload profileImage, Upload backgroundImage) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.introduction = introduction;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        // :TODO email 구현 시 변경
        this.certified = Certified.CERTIFIED;
    }

    public void updateCertified() {
        if (this.certified == Certified.NOT_CERTIFIED) {
            this.certified = Certified.CERTIFIED;
        } else {
            throw new IllegalArgumentException("ALREADY CERTIFIED");
        }

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

    public void updateBackgroundImage(Upload background) {
        this.backgroundImage = background;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void delete() {
        super.delete();
    }
}
