package day.dayBackend.dto.crawling;

import lombok.Getter;

import java.util.List;

@Getter
public class HabitRecommendResponseDto {

    private Long totalRows; // 습관 수
    private Long totalPages; // 페이지 수
    List<HabitRecommendListDto> habitRecommendList;


    public static HabitRecommendResponseDto of(long totalPages, long totalRows, List<HabitRecommendListDto> habitRecommendList) {
        HabitRecommendResponseDto dto = new HabitRecommendResponseDto();
        dto.totalPages = totalPages;
        dto.totalRows = totalRows;
        dto.habitRecommendList = habitRecommendList;
        return dto;
    }

}
