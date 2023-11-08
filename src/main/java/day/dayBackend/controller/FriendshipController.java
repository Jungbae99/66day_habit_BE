package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.FriendDetailResponseDto;
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
     * 친구의 회원 정보 조회
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<FriendDetailResponseDto> getFriendInfoV1(@RequestParam(name = "friendId") Long friendId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<FriendDetailResponseDto>builder()
                .data(friendshipService.getFriendDetailById(memberId, friendId))
                .build();
    }

    /**
     * 친구 검색
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<FriendDetailResponseDto> searchFriendV1(@RequestParam(name = "search") String search) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<FriendDetailResponseDto>builder()
                .data(friendshipService.getFriendDetailBySearch(memberId, search))
                .build();
    }


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
    @DeleteMapping("/{friendId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto<Map<String, Long>> deleteFriendV1(@PathVariable(name = "friendId") Long friendId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("followId", friendshipService.deleteFriend(memberId, friendId)))
                .build();
    }
}
