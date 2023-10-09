package day.dayBackend.service;

import day.dayBackend.domain.habit.Habit;
import day.dayBackend.domain.habit.HabitRecord;
import day.dayBackend.dto.request.habit.HabitRecordRequestDto;
import day.dayBackend.dto.response.habit.HabitRecordResponseDto;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.HabitRecordRepository;
import day.dayBackend.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        HabitRecord habitRecord = HabitRecord.builder()
                .dayNumber(dto.getDayNumber())
                .achievementRate(dto.getAchievementRate())
                .habit(habit)
                .build();

        habitRecordRepository.save(habitRecord);

        return habitRecord.getDayNumber();
    }
}
