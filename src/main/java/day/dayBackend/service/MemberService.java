package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.dto.request.MemberUpdateRequestDto;
import day.dayBackend.dto.response.MemberDetailResponseDto;
import day.dayBackend.dto.response.MemberResponseDto;
import day.dayBackend.dto.response.MemberUpdateResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final UploadService uploadService;

    /**
     * 메인페이지 회원정보 조회
     */
    public MemberResponseDto getMemberById(Long id) {
        return MemberResponseDto.fromEntity(
                memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(
                        () -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다.")));
    }

    /**
     * 회원 정보 상세 조회
     */
    public MemberDetailResponseDto getMemberDetailById(Long id) {
        return MemberDetailResponseDto.fromEntity(
                memberRepository.findByIdAndDeletedAtNull(id).orElseThrow(
                        () -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다")));
    }

    /**
     * 회원 수정
     */
    @Transactional
    public MemberUpdateResponseDto updateMember(Long id, MemberUpdateRequestDto dto) {
        System.out.println("dto.getUsername() = " + dto.getUsername());
        System.out.println("dto.getIntroduction() = " + dto.getIntroduction());
//        System.out.println("dto.getProfileImage() = " + dto.getProfileImage());

        Member member = memberRepository.findByIdAndDeletedAtNull(id)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다"));
//
        if (dto.getUsername().isPresent()) {
            member.updateUsername(dto.getUsername().get());
        }
        if (dto.getIntroduction().isPresent()) {
            member.updateIntroduction(dto.getIntroduction().get());
        }
//        if (dto.getProfileImage().isPresent()) {
//            MultipartFile profileImage = dto.getProfileImage().get();
//            uploadService.uploadFile(id, profileImage);
//        }

        return MemberUpdateResponseDto.fromEntity(member);
    }
}
