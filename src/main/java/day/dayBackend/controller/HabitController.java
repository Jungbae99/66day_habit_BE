package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.request.habit.HabitCreateRequestDto;
import day.dayBackend.dto.request.habit.HabitUpdateRequestDto;
import day.dayBackend.dto.response.CommonResponseDto;

import day.dayBackend.dto.response.habit.HabitDetailResponseDto;
import day.dayBackend.dto.response.habit.HabitListResponseDto;
import day.dayBackend.dto.response.habit.HabitUpdateResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.search.HabitSearch;
import day.dayBackend.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/habit")
public class HabitController {

    private final HabitService habitService;

    /**
     * 습관 검색 (정렬조건 앞에 -를 붙이면 내림차순, 붙이면 오름차순)
     */
    @GetMapping("")
    public CommonResponseDto<HabitListResponseDto> getHabitListV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                  @RequestParam(value = "limit", required = false, defaultValue = "100") int size,
                                                                  @RequestParam(value = "search1", required = false) String keyword1,
                                                                  @RequestParam(value = "search2", required = false) String keyword2,
                                                                  @RequestParam(value = "sort", required = false) String sort
    ) {
        Pageable pageable = PageRequest.of(page, size);
        HabitSearch search = HabitSearch.builder().keyword1(keyword1).keyword2(keyword2).sort(sort).build();
        return CommonResponseDto.<HabitListResponseDto>builder()
                .data(habitService.getHabitList(pageable, search))
                .build();
    }

    /**
     * 습관 더 보기 정보 조회
     */
    @GetMapping("/detail")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto getHabitDetailV1(@RequestParam(value = "habitId") Long habitId) {
        return CommonResponseDto.<HabitDetailResponseDto>builder()
                .data(habitService.getHabitDetail(habitId))
                .build();
    }

    /**
     * 새로운 습관 조회
     */
    @GetMapping("/new")
    public CommonResponseDto getNewestHabitV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<HabitListResponseDto>builder()
                .data(habitService.getNewestHabit(pageable))
                .build();
    }

    /**
     * 완료한 습관 조회
     */
    @GetMapping("/done")
    public CommonResponseDto getDoneHabitV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<HabitListResponseDto>builder()
                .data(habitService.getDoneHabit(pageable))
                .build();
    }

    /**
     * 유명한 습관 목록 조회
     */
    @GetMapping("/famous")
    public CommonResponseDto getFamousHabitV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<HabitListResponseDto>builder()
                .data(habitService.getFamousHabit(pageable))
                .build();
    }

    /**
     * 습관 생성
     */
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
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
    @PreAuthorize("hasAnyRole('ROLE_USER')")
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
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto deleteHabitV1(@PathVariable(name = "habitId") Long habitId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitId", habitService.deleteHabit(memberId, habitId)))
                .build();
    }

    /**
     * 관리자 습관 등록
     */
    @PostMapping("/famous")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CommonResponseDto createFamousHabitV1(@RequestBody final HabitCreateRequestDto dto) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitId", habitService.createHabit(memberId, dto)))
                .build();
    }

}
