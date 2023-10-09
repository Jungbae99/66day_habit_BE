package day.dayBackend.controller;

import day.dayBackend.dto.request.habit.HabitCreateRequestDto;
import day.dayBackend.dto.request.habit.HabitUpdateRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.habit.HabitUpdateResponseDto;
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
    public CommonResponseDto<Map<String, Long>> createHabitV1(@RequestBody final HabitCreateRequestDto dto, @RequestParam(value = "id") Long id) {
        // :TODO Security member PK 검증
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitId", habitService.createHabit(id, dto)))
                .build();
    }

    /**
     * 습관 수정
     */
    @PatchMapping("/{habitId}")
    public CommonResponseDto<HabitUpdateResponseDto> updateHabitV1(@RequestBody final HabitUpdateRequestDto dto, @PathVariable(name = "habitId") Long habitId) {
        return CommonResponseDto.<HabitUpdateResponseDto>builder()
                // :TODO Security PK 꺼내서 수정권한을 추가하자. (memberId 도 넘기자)
                .data(habitService.updateHabit(habitId, dto))
                .build();
    }

    /**
     * 습관 삭제
     * @DeleteMapping("")
     *     public CommonResponseDto resignV1(@RequestParam(value = "id") Long id, @RequestBody final MemberDeleteRequestDto dto) {
     *         memberService.resign(id, dto);
     *         return CommonResponseDto.builder().build();
     *     }
     */
    @DeleteMapping("/{habitId}")
    public CommonResponseDto deleteHabitV1(@PathVariable(name = "habitId") Long habitId) {
        // :TODO Security PK 꺼내서 수정권한을 추가하자.
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitId", habitService.deleteHabit(habitId)))
                .build();
    }

}
