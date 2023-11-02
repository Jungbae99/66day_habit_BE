package day.dayBackend.dto.crawling;

import lombok.Getter;

import java.util.List;

@Getter
public class QuoteRecommendResponseDto {

    private Long totalRows; // 습관 수
    private Long totalPages; // 페이지 수
    List<QuoteRecommendListDto> quoteRecommendList;


    public static QuoteRecommendResponseDto of(long totalPages, long totalRows, List<QuoteRecommendListDto> quoteRecommendList) {
        QuoteRecommendResponseDto dto = new QuoteRecommendResponseDto();
        dto.totalPages = totalPages;
        dto.totalRows = totalRows;
        dto.quoteRecommendList = quoteRecommendList;
        return dto;
    }
}
