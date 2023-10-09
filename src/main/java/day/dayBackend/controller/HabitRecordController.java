package day.dayBackend.controller;

import day.dayBackend.dto.request.habit.HabitRecordRequestDto;
import day.dayBackend.dto.response.habit.HabitRecordResponseDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.service.HabitRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/habit/record")
public class HabitRecordController {

    private final HabitRecordService habitRecordService;

    /**
     * 습관 기록 조회
     */
    @GetMapping("/{habitId}")
    public CommonResponseDto<List<HabitRecordResponseDto>> getHabitRecordV1(@PathVariable(name = "habitId") Long habitId) {
        return CommonResponseDto.<List<HabitRecordResponseDto>>builder()
                .data(habitRecordService.getHabitRecordList(habitId))
                .build();
    }

    /**
     * 습관 기록 등록
     */
    @PostMapping("/{habitId}")
    public CommonResponseDto<Map<String, Integer>> createHabitRecord(@PathVariable(name = "habitId") Long habitId,
                                                                      @RequestBody HabitRecordRequestDto dto) {
        return CommonResponseDto.<Map<String, Integer>>builder()
                .data(Map.of("dayNumber", habitRecordService.createHabitRecord(habitId, dto)))
                .build();
    }
}
