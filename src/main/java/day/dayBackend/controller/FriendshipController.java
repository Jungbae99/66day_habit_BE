package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.FriendshipResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.service.FriendshipService;
import day.dayBackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/friend")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final MemberService memberService;

    /**
     * follow 추가
     */
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Long>> createHabitLikesV1(@RequestParam(name = "followId") Long followId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("followId", friendshipService.followFriend(memberId, followId)))
                .build();
    }

    /**
     * 내가 follow 하는 Member 조회
     */
    @GetMapping("/my/following")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<List<FriendshipResponseDto>> getMyFollowerV1(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<List<FriendshipResponseDto>>builder()
                .data(friendshipService.getFollowList(memberId, pageable))
                .build();
    }

    /**
     * 친구 삭제
     */
    @DeleteMapping("/{followId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Long>> deleteFriendV1(@PathVariable(name = "followId") Long followId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("followId", friendshipService.deleteFriend(memberId, followId)))
                .build();
    }
}