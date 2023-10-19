package day.dayBackend.service;

import day.dayBackend.domain.FileCategory;
import day.dayBackend.domain.Member;
import day.dayBackend.domain.Upload;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.MemberRepository;
import day.dayBackend.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UploadService {

    private final CloudStorageService cloudStorageService;
    private final UploadRepository uploadRepository;
    private final MemberRepository memberRepository;

    /**
     * 파일 업로드
     */
    @Transactional
    public Long uploadFile(Long memberId, MultipartFile file) throws IOException {

        FileCategory category = FileCategory.PROFILE;

        // 영속화
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        String originName = file.getOriginalFilename() == null ? "tmp" : file.getOriginalFilename();
        String savedName = generateSavedName(originName);
        String filePath = category.toString().toLowerCase() + "/" + savedName;
        String savedUrl = cloudStorageService.getUrl(filePath);

        Upload upload = Upload.builder()
                .member(member)
                .url(savedUrl)
                .originName(originName)
                .savedName(savedName)
                .type(file.getContentType()) // MIME 타입
                .extension(getFileExtension(originName))
                .size(file.getSize())
                .build();

        cloudStorageService.uploadFile(file, filePath);
        Upload savedUpload = uploadRepository.save(upload);

        return savedUpload.getId();
    }

    /**
     * 랜덤 파일명
     */
    private String generateSavedName(String originName) {
        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(originName);
        return uniqueId + "-" + System.currentTimeMillis() + "." + fileExtension;
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf('.');
        assert dotIndex >= 0 && dotIndex < originalFilename.length() - 1;

        return originalFilename.substring(dotIndex + 1).toLowerCase();
    }


}
