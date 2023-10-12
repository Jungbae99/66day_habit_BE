package day.dayBackend.domain;

import day.dayBackend.domain.habit.BaseAuditingListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Upload extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String url;

    private String originName;

    private String savedName;

    private String type;

    private String extension;

    private Long size;

    @Builder
    Upload(String url, Member member, String originName, String savedName, String type, String extension, Long size) {
        this.url = url;
        this.member = member;
        this.originName = originName;
        this.savedName = savedName;
        this.type = type;
        this.extension = extension;
        this.size = size;
    }

    public void delete() {
        super.delete();
    }

}
