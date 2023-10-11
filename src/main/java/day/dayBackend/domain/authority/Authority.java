package day.dayBackend.domain.authority;

import day.dayBackend.domain.habit.BaseAuditingListener;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String authorityName;

    public static Authority createAuthority(String authorityName) {
        Authority authority = new Authority();
        authority.authorityName = authorityName;
        return authority;
    }

    public void updateAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String toString() {
        return authorityName;
    }

}
