package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.*;
import day.dayBackend.dto.request.habit.HabitCreateRequestDto;
import day.dayBackend.dto.request.habit.HabitUpdateRequestDto;
import day.dayBackend.dto.response.habit.HabitDetailResponseDto;
import day.dayBackend.dto.response.habit.HabitListResponseDto;
import day.dayBackend.dto.response.habit.HabitSummaryResponseDto;
import day.dayBackend.dto.response.habit.HabitUpdateResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.HabitRepository;
import day.dayBackend.repository.MemberRepository;
import day.dayBackend.repository.custom.HabitRepositoryImpl;
import day.dayBackend.search.HabitSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitService {

    private final HabitRepository habitRepository;
    private final MemberRepository memberRepository;
    private final HabitRepositoryImpl habitSearchRepoImpl;

    /**
     * 습관 생성
     */
    @Transactional
    public Long createHabit(Long memberId, HabitCreateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 회원이 존재하지 않습니다."));

        validateHabitDuplication(dto.getHabitName(), memberId);

        Habit habit = Habit.builder()
                .member(member)
                .habitName(dto.getHabitName())
                .backgroundColor(BackgroundColor.valueOf(dto.getBackgroundColor()))
                .fontColor(FontColor.valueOf(dto.getFontColor()))
                .habitVisibility(HabitVisibility.valueOf(dto.getHabitVisibility()))
                .habitTag(dto.getHabitTag())
                .build();

        habitRepository.save(habit);

        return habit.getId();
    }

    /**
     * 습관 수정
     */
    @Transactional
    public HabitUpdateResponseDto updateHabit(Long memberId, Long habitId, HabitUpdateRequestDto dto) {

        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 습관을 찾을 수 없습니다."));

        if (!habit.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        if (dto.getHabitName().isPresent()) {
            validateHabitDuplication(dto.getHabitName().get(), memberId);
            habit.updateHabitName(dto.getHabitName().get());
        }
        if (dto.getFontColor().isPresent()) {
            habit.updateFontColor(dto.getFontColor().get());
        }
        if (dto.getBackgroundColor().isPresent()) {
            habit.updateBackgroundColor(dto.getBackgroundColor().get());
        }
        if (dto.getHabitVisibility().isPresent()) {
            habit.updateHabitVisibility(dto.getHabitVisibility().get());
        }
        if (dto.getHabitTag().isPresent()) {
            habit.updateHabitTag(dto.getHabitTag().get());
        }

        return HabitUpdateResponseDto.fromEntity(habit);
    }


    /**
     * 습관 삭제
     */
    @Transactional
    public Long deleteHabit(Long memberId, Long habitId) {
        // member 영속화
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitId)
                .orElseThrow(() -> new NotFoundException("해당하는 습관이 존재하지 않습니다."));

        if (!habit.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }
        
        habit.delete();
        return habit.getId();
    }

    /**
     * 습관 검색
     */
    public HabitListResponseDto getHabitList(Pageable pageable, HabitSearch habitSearch) {
        Page<Habit> habits = habitSearchRepoImpl.findByDeletedAtNull(pageable, habitSearch);
        return HabitListResponseDto.of(
                habits.getTotalElements(),
                habits.getTotalPages(),
                habits.getContent().stream()
                        .map(HabitSummaryResponseDto::fromEntity)
                        .collect(Collectors.toList()));
    }

    /**
     * 습관 더보기 조회
     */
    public HabitDetailResponseDto getHabitDetail(Long habitId) {
        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitId)
                .orElseThrow(() -> new NotFoundException("해당하는 습관이 존재하지 않습니다."));

        boolean todayCheck = checkTodayDone(habit.getHabitRecords());
        int inspireDay = checkInspire(habit);

        return HabitDetailResponseDto.of(habit, todayCheck, inspireDay);
    }

    /**
     * 유명한 습관 목록 조회
     */
    public HabitListResponseDto getFamousHabit(Pageable pageable) {
        Page<Habit> habitList = habitRepository.findFamousHabit(pageable);

        return HabitListResponseDto.of(
                habitList.getTotalElements(),
                habitList.getTotalPages(),
                habitList.getContent().stream().
                map(HabitSummaryResponseDto::fromEntity).collect(Collectors.toList()));
    }

    /**
     * 최근에 생긴 습관 조회
     */
    public HabitListResponseDto getNewestHabit(Pageable pageable) {
        Page<Habit> habitList = habitRepository.findNewestHabit(pageable);

        return HabitListResponseDto.of(
                habitList.getTotalElements(),
                habitList.getTotalPages(),
                habitList.getContent().stream().
                        map(HabitSummaryResponseDto::fromEntity).collect(Collectors.toList()));
    }

    /**
     * 66일 모두 기록한 습관 조회
     */
    public HabitListResponseDto getDoneHabit(Pageable pageable) {
        Page<Habit> habitList = habitRepository.findDoneHabit(pageable);

        return HabitListResponseDto.of(
                habitList.getTotalElements(),
                habitList.getTotalPages(),
                habitList.getContent().stream().
                        map(HabitSummaryResponseDto::fromEntity).collect(Collectors.toList()));
    }

    /**
     * 중복체크 유틸
     */
    private void validateHabitDuplication(String habitName, Long memberId) throws DuplicateKeyException {
        habitRepository.findByMemberIdAndHabitNameAndDeletedAtIsNull(memberId, habitName)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("같은 이름의 습관이 존재합니다.");
                });
    }

    /**
     * 당일 습관 체크 여부 확인
     */
    private boolean checkTodayDone(List<HabitRecord> habitRecords) {
        LocalDateTime time = LocalDateTime.now();
        for (HabitRecord habitRecord : habitRecords) {
            if (time.getDayOfYear() == habitRecord.getCreatedAt().getDayOfYear()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 3, 7, 14일 확인
     */
    private int checkInspire(Habit habit) {

        LocalDateTime habitTime = habit.getCreatedAt();
        LocalDateTime currentTime = LocalDateTime.now();

        long between = ChronoUnit.HOURS.between(habitTime, currentTime);
        int recordsAllowed = (int) ((between / 24) + 1);

        if (recordsAllowed == 3) {
            return 3;
        } else if (recordsAllowed == 7) {
            return 7;
        } else if (recordsAllowed == 14) {
            return 14;
        } else {
            return 0;
        }
    }


}
