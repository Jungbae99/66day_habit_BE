package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.habit.BackgroundColor;
import day.dayBackend.domain.habit.FontColor;
import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.HabitVisibility;
import day.dayBackend.dto.request.habit.HabitCreateRequestDto;
import day.dayBackend.dto.request.habit.HabitUpdateRequestDto;
import day.dayBackend.dto.response.habit.HabitUpdateResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.HabitRepository;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitService {

    private final HabitRepository habitRepository;
    private final MemberRepository memberRepository;

    /**
     * 습관 생성
     */
    @Transactional
    public Long createHabit(Long memberId, HabitCreateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 회원이 존재하지 않습니다."));

        validateHabitDuplication(dto.getHabitName());

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
    public HabitUpdateResponseDto updateHabit(Long id, HabitUpdateRequestDto dto) {

        Habit habit = habitRepository.findByIdAndDeletedAtNull(id)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 습관을 찾을 수 없습니다."));

        if (dto.getHabitName().isPresent()) { validateHabitDuplication(dto.getHabitName().get()); habit.updateHabitName(dto.getHabitName().get()); }
        if (dto.getFontColor().isPresent()) { habit.updateFontColor(dto.getFontColor().get()); }
        if (dto.getBackgroundColor().isPresent()) { habit.updateBackgroundColor(dto.getBackgroundColor().get()); }
        if (dto.getHabitVisibility().isPresent()) { habit.updateHabitVisibility(dto.getHabitVisibility().get()); }
        if (dto.getHabitTag().isPresent()) { habit.updateHabitTag(dto.getHabitTag().get()); }

        return HabitUpdateResponseDto.fromEntity(habit);
    }


    /**
     * 습관 삭제
     */
    @Transactional
    public Long deleteHabit(Long habitId) {
        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitId)
                .orElseThrow(() -> new NotFoundException("해당하는 습관이 존재하지 않습니다."));
        
        // member & habit 비교
        habit.delete();
        return habit.getId();
    }

    /**
     * 중복체크 유틸
     */
    private void validateHabitDuplication(String habitName) throws DuplicateKeyException {
        habitRepository.findByHabitNameAndDeletedAtNull(habitName)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("같은 이름의 습관이 존재합니다.");
                });
    }

    
}
