package day.dayBackend.domain.authority;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.BaseAuditingListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    private MemberAuthority(Member member, Authority authority) {
        this.member = member;
        this.authority = authority;
    }

    public static MemberAuthority createMemberAuthority(Member member, Authority authority) {
        return new MemberAuthority(member, authority);
    }

    /**
     * 권한 부여
     */
    public static MemberAuthority addAuthority(Member member, Authority authority) {
        return new MemberAuthority(member, authority);
    }

    public Authority toAuthority() {
        return this.authority;
    }

    public void delete() {
        super.delete();
    }

    @Override
    public String toString() {
        return this.authority.getAuthorityName();
    }
}
