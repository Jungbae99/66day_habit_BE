package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.request.habit.HabitCreateRequestDto;
import day.dayBackend.dto.request.habit.HabitUpdateRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.habit.HabitUpdateResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.service.HabitService;
import day.dayBackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/habit")


public class HabitController {

    private final HabitService habitService;
    private final MemberService memberService;

    /**
     * 습관 생성
     */
    @PostMapping("")
    public CommonResponseDto<Map<String, Long>> createHabitV1(@RequestBody final HabitCreateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitId", habitService.createHabit(memberId, dto)))
                .build();
    }

    /**
     * 습관 수정
     */
    @PatchMapping("/{habitId}")
    public CommonResponseDto<HabitUpdateResponseDto> updateHabitV1(@RequestBody final HabitUpdateRequestDto dto, @PathVariable(name = "habitId") Long habitId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));

        return CommonResponseDto.<HabitUpdateResponseDto>builder()
                .data(habitService.updateHabit(memberId, habitId, dto))
                .build();
    }

    /**
     * 습관 삭제
     */
    @DeleteMapping("/{habitId}")
    public CommonResponseDto deleteHabitV1(@PathVariable(name = "habitId") Long habitId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitId", habitService.deleteHabit(memberId, habitId)))
                .build();
    }

}
