package day.dayBackend.service;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.HabitRecord;
import day.dayBackend.dto.request.habit.HabitRecordRequestDto;
import day.dayBackend.dto.request.habit.HabitRecordUpdateRequestDto;
import day.dayBackend.dto.response.habit.HabitRecordResponseDto;
import day.dayBackend.dto.response.habit.HabitRecordUpdateResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.HabitRecordRepository;
import day.dayBackend.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HabitRecordService {

    private final HabitRecordRepository habitRecordRepository;
    private final HabitRepository habitRepository;

    /**
     * 습관에 달린 기록 조회
     */
    public List<HabitRecordResponseDto> getHabitRecordList(Long habitId) {
        return habitRecordRepository.getHabitRecords(habitId)
                .stream().map(HabitRecordResponseDto::fromEntity).toList();
    }


    /**
     * 습관 기록 생성
     */
    @Transactional
    public Integer createHabitRecord(Long habitId, HabitRecordRequestDto dto) {
        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 습관을 찾을 수 없습니다."));

        if (!checkRecordValidation(habitId, dto.getDayNumber())) {
            throw new IllegalArgumentException("습관 기록 생성이 허용되지 않습니다.");
        }

        HabitRecord habitRecord = HabitRecord.builder()
                .dayNumber(dto.getDayNumber())
                .achievementRate(dto.getAchievementRate())
                .habit(habit)
                .build();

        habitRecordRepository.save(habitRecord);

        return habitRecord.getDayNumber();
    }

    /**
     * 습관 기록 수정
     */
    @Transactional
    public HabitRecordUpdateResponseDto updateRecordV1(Long habitId, Integer dayNumber, HabitRecordUpdateRequestDto dto) {
        HabitRecord habitRecord = habitRecordRepository.findByHabitId(habitId, dayNumber)
                .orElseThrow(NotFoundException::new);

        habitRecord.updateAchievement(dto.getAchievementRate());
        return HabitRecordUpdateResponseDto.fromEntity(habitRecord);
    }

    /**
     * 습관 기록 삭제
     */
    @Transactional
    public Integer deleteHabitRecord(Long habitId, Integer dayNumber) {
        HabitRecord habitRecord = habitRecordRepository.findByHabitId(habitId, dayNumber)
                .orElseThrow(NotFoundException::new);

        habitRecord.delete();

        return dayNumber;
    }

    /**
     * 날짜 검증 로직
     */
    public boolean checkRecordValidation(Long habitId, int dayNumber) {
        Habit habit = habitRepository.findByIdAndDeletedAtNull(habitId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 습관을 찾을 수 없습니다."));

        LocalDateTime createdAt = habit.getCreatedAt();
        LocalDateTime currentTime = LocalDateTime.now();

        long between = ChronoUnit.HOURS.between(createdAt, currentTime);
        int recordsAllowed = (int) ((between / 24) + 1);

        if (dayNumber > recordsAllowed) {
            return false;
        }
        return true;
    }
    
}