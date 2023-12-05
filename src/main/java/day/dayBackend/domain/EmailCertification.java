package day.dayBackend.domain;

import day.dayBackend.domain.habit.BaseAuditingListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCertification extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_certification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String certCode;

    private EmailCertification(Member member) {
        this.member = member;
        this.email = member.getEmail();
        this.certCode = createCertCode();
    }

    public static EmailCertification createEmailCertification(Member member) {
        return new EmailCertification(member);
    }

    public boolean isExpired() {
        return Duration.between(super.getCreatedAt(), LocalDateTime.now())
                .compareTo(Duration.ofSeconds(3000)) // 만료시간 5분
                >= 0;
    }

    public void delete() {
        super.delete();
    }

    private String createCertCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

}
