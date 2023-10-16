package day.dayBackend.service;

import day.dayBackend.domain.HabitLikes;
import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.Habit;
import day.dayBackend.dto.response.habit.HabitLikesResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.HabitLikesRepository;
import day.dayBackend.repository.HabitRepository;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HabitLikesService {

    private final MemberRepository memberRepository;
    private final HabitRepository habitRepository;
    private final HabitLikesRepository habitLikesRepository;

    /**
     * 좋아요 목록 조회
     */
    public List<HabitLikesResponseDto> getLikesList(Long memberId, Pageable pageable) {
        Page<HabitLikes> likesPage = habitLikesRepository.findHabitLikesByMemberId(memberId, pageable);
        List<HabitLikesResponseDto> likesList = likesPage.stream()
                .map(HabitLikesResponseDto::fromEntity)
                .collect(Collectors.toList());
        return likesList;
    }
    
    
    /**
     * 좋아요
     */
    @Transactional
    public Long createLikes(Long memberId, Long habitLikesId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitLikesId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 습관을 찾을 수 없습니다."));

        if (habitLikesRepository.findByMemberIdAndHabitLikesIdAndDeletedAtNull(member.getId(), habit.getId()).isPresent()) {
            throw new DuplicateKeyException("이미 등록한 좋아요입니다.");
        }

        HabitLikes savedLikes = habitLikesRepository.save(HabitLikes.createLikes(member, habit));
        return savedLikes.getId();
    }


    /**
     * 좋아요 삭제
     */
    @Transactional
    public Long deleteLikes(Long memberId, Long habitLikesId) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        HabitLikes habitLikes = habitLikesRepository.findByIdAndDeletedAtNull(habitLikesId)
                .orElseThrow(() -> new NotFoundException("좋아요 데이터를 찾을 수 없습니다."));

        if (!habitLikes.getMember().equals(member)) {
            throw new AccessDeniedException("ACCESS DENIED");
        }

        habitLikes.delete();
        return habitLikes.getId();
    }
}
