package day.dayBackend.dto.recommend;

import lombok.Getter;

import java.util.List;

@Getter
public class HabitRecommendResponseDto {

    private Long totalRows; // 습관 수
    private Long totalPages; // 페이지 수
    List<RecommendedHabitDto> habitRecommendList;


    public static HabitRecommendResponseDto of(long totalPages, long totalRows, List<RecommendedHabitDto> habitRecommendList) {
        HabitRecommendResponseDto dto = new HabitRecommendResponseDto();
        dto.totalPages = totalPages;
        dto.totalRows = totalRows;
        dto.habitRecommendList = habitRecommendList;
        return dto;
    }

}
