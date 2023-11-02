package day.dayBackend.dto.crawling;

import day.dayBackend.domain.crawling.RecommendedQuote;
import lombok.Getter;

@Getter
public class QuoteRecommendListDto {

    private String wiseSaying;
    private String greatPerson;


    public static QuoteRecommendListDto fromEntity(RecommendedQuote recommendedQuote) {
        QuoteRecommendListDto dto = new QuoteRecommendListDto();
        dto.wiseSaying = recommendedQuote.getWiseSaying();
        dto.greatPerson = recommendedQuote.getGreatPerson();
        return dto;
    }
}
