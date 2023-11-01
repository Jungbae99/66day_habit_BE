package day.dayBackend.controller;

import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.recommend.HabitRecommendResponseDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.dto.response.habit.HabitListResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import day.dayBackend.service.HabitRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/recommend")
public class RecommendController {

    private final HabitRecommendService habitRecommendService;

    @GetMapping("/habit")
    public CommonResponseDto<HabitRecommendResponseDto> getHabitRecommendV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                            @RequestParam(value = "limit", required = false, defaultValue = "100") int size) {

        habitRecommendService.executeCardCrawlerOnStartup();
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<HabitRecommendResponseDto>builder()
                .data(habitRecommendService.getHabitList(memberId, pageable))
                .build();

    }

}
