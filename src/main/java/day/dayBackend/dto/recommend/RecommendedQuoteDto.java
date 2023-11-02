package day.dayBackend.dto.recommend;

import day.dayBackend.domain.recommend.RecommendedQuote;
import lombok.Getter;

@Getter
public class RecommendedQuoteDto {

    private String wiseSaying;
    private String greatPerson;


    public static RecommendedQuoteDto fromEntity(RecommendedQuote recommendedQuote) {
        RecommendedQuoteDto dto = new RecommendedQuoteDto();
        dto.wiseSaying = recommendedQuote.getWiseSaying();
        dto.greatPerson = recommendedQuote.getGreatPerson();
        return dto;
    }
}
