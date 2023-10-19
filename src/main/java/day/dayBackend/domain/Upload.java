package day.dayBackend.domain;

import day.dayBackend.domain.habit.BaseAuditingListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Column(name = "upload_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Size(max = 512)
    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileCategory fileCategory;

    private String originName;

    @Column(nullable = false)
    private String savedName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Builder
    Upload(String url, Member member, String originName, String savedName, String type, String extension, Long size) {
        this.url = url;
        this.member = member;
        this.originName = originName;
        this.fileCategory = FileCategory.PROFILE;
        this.savedName = savedName;
        this.type = type;
        this.extension = extension;
        this.size = size;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

    public void updateFileCategory(FileCategory fileCategory) {
        this.fileCategory = fileCategory;
    }

    public void updateOriginName(String originName) {
        this.originName = originName;
    }

    public void updateSavedName(String savedName) {
        this.savedName = savedName;
    }

    public void updateType(String type) {
        this.type = type;
    }

    public void updateExtension(String extension) {
        this.extension = extension;
    }

    public void updateSize(Long size) {
        this.size = size;
    }

    public void delete() {
        super.delete();
    }

}
