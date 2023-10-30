package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.request.habit.HabitRecordRequestDto;
import day.dayBackend.dto.request.habit.HabitRecordUpdateRequestDto;
import day.dayBackend.dto.response.habit.HabitRecordResponseDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.habit.HabitRecordUpdateResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.service.HabitRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/habit/record")
public class HabitRecordController {

    private final HabitRecordService habitRecordService;

    /**
     * 습관 기록 조회
     */
    @GetMapping("/{habitId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<List<HabitRecordResponseDto>> getHabitRecordV1(@PathVariable(name = "habitId") Long habitId) {
        return CommonResponseDto.<List<HabitRecordResponseDto>>builder()
                .data(habitRecordService.getHabitRecordList(habitId))
                .build();
    }

    /**
     * 습관 기록 등록
     */
    @PostMapping("/{habitId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Integer>> createHabitRecordV1(@PathVariable(name = "habitId") Long habitId,
                                                                       @RequestBody HabitRecordRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Integer>>builder()
                .data(Map.of("dayNumber", habitRecordService.createHabitRecord(memberId, habitId, dto)))
                .build();
    }

    /**
     * 습관 기록 수정
     */
    @PutMapping("/{habitId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<HabitRecordUpdateResponseDto> updateHabitRecordV1(@PathVariable(name = "habitId") Long habitId,
                                                                               @RequestParam(value = "dayNumber") Integer dayNumber,
                                                                               @RequestBody HabitRecordUpdateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<HabitRecordUpdateResponseDto>builder()
                .data(habitRecordService.updateRecordV1(memberId, habitId, dayNumber, dto))
                .build();
    }

    /**
     * 습관 기록 삭제
     */
    @DeleteMapping("/{habitId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Integer>> deleteHabitRecordV1(@PathVariable(name = "habitId") Long habitId,
                                                                       @RequestParam(value = "dayNumber") Integer dayNumber) {
        return CommonResponseDto.<Map<String, Integer>>builder()
                .data(Map.of("dayNumber", habitRecordService.deleteHabitRecord(habitId, dayNumber)))
                .build();
    }
}
