package day.dayBackend.controller;

import day.dayBackend.dto.recommend.HabitRecommendResponseDto;
import day.dayBackend.dto.recommend.QuoteRecommendResponseDto;
import day.dayBackend.dto.recommend.RecommendedHabitDto;
import day.dayBackend.dto.recommend.RecommendedQuoteDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.service.recommend.HabitRecommendService;
import day.dayBackend.service.recommend.QuoteRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recommend")
public class RecommendController {

    private final HabitRecommendService habitRecommendService;
    private final QuoteRecommendService quoteRecommendService;

    /**
     * 랜덤 습관 조회
     */
    @GetMapping("/random/habit")
    public CommonResponseDto<List<RecommendedHabitDto>> getRandomHabitV1() {
        return CommonResponseDto.<List<RecommendedHabitDto>>builder()
                .data(habitRecommendService.getRandomList())
                .build();
    }

    /**
     * 랜덤 습관 조회
     */
    @GetMapping("/random/quote")
    public CommonResponseDto<RecommendedQuoteDto> getRandomQuoteV1() {
        return CommonResponseDto.<RecommendedQuoteDto>builder()
                .data(quoteRecommendService.getRandomList())
                .build();
    }

    /**
     * 습관 크롤링
     */
    @GetMapping("/crawling/habit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CommonResponseDto<HabitRecommendResponseDto> getHabitRecommendV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                            @RequestParam(value = "limit", required = false, defaultValue = "100") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<HabitRecommendResponseDto>builder()
                .data(habitRecommendService.getHabitList(pageable))
                .build();

    }

    /**
     * 명언 크롤링
     */
    @GetMapping("/crawling/quote")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CommonResponseDto<QuoteRecommendResponseDto> getQuoteRecommendV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                            @RequestParam(value = "limit", required = false, defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<QuoteRecommendResponseDto>builder()
                .data(quoteRecommendService.getQuoteList(pageable))
                .build();
    }

}
