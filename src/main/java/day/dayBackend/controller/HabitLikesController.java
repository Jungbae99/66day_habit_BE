package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.habit.HabitLikesResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.service.HabitLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/likes")
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class HabitLikesController {

    private final HabitLikesService habitLikesService;

    /**
     * 습관 좋아요
     */
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Long>> createHabitLikesV1(@RequestParam(name = "habitId") Long habitId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitLikesId", habitLikesService.createLikes(memberId, habitId)))
                .build();
    }

    /**
     * 좋아요 목록 조회
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<List<HabitLikesResponseDto>> getMyHabitLikesV1(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<HabitLikesResponseDto>>builder()
                .data(habitLikesService.getLikesList(memberId, pageable))
                .build();
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping("/{habitLikesId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Long>> deleteHabitLikesV1(@PathVariable(name = "habitLikesId") Long habitLikesId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID_ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("habitLikesId", habitLikesService.deleteLikes(memberId, habitLikesId)))
                .build();
    }
}
