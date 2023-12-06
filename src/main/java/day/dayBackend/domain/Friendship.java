package day.dayBackend.domain;

import day.dayBackend.domain.habit.BaseAuditingListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // member 가 Follow 하고 있는 사람들
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member following;

    // member 를 Following 하고 있는 사람들
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower;

    @Builder
    Friendship(Member following, Member follower) {
        this.following = following;
        this.follower = follower;
    }

    public void delete() {
        super.delete();
    }
}
