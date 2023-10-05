package day.dayBackend.service;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UploadService {

    private UploadRepository uploadRepository;
    private MemberRepository memberRepository;

    private final String uploadDir = "C:/66day/upload";

    /**
     * 파일 업로드
     */
    public Long uploadFile(Long memberId, MultipartFile file) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Path.of(uploadDir, uniqueFileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중에 오류가 발생했습니다.");
        }

        Upload upload = Upload.builder()
                .member(member)
                .url(uploadDir)
                .originName(originalFilename)
                .savedName(uniqueFileName)
                .type(file.getContentType())
                .extension(getFileExtension(originalFilename))
                .size(file.getSize())
                .build();

        return uploadRepository.save(upload).getId();
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
