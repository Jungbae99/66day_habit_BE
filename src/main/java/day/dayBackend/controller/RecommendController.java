package day.dayBackend.controller;

import day.dayBackend.dto.crawling.HabitRecommendResponseDto;
import day.dayBackend.dto.crawling.QuoteRecommendResponseDto;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.service.crawling.HabitRecommendService;
import day.dayBackend.service.crawling.QuoteRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/recommend")
public class RecommendController {

    private final HabitRecommendService habitRecommendService;
    private final QuoteRecommendService quoteRecommendService;




    /**
     * 습관 크롤링
     */
    @GetMapping("/habit")
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
    @GetMapping("/quote")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CommonResponseDto<QuoteRecommendResponseDto> getQuoteRecommendV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                            @RequestParam(value = "limit", required = false, defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<QuoteRecommendResponseDto>builder()
                .data(quoteRecommendService.getQuoteList(pageable))
                .build();
    }

}
