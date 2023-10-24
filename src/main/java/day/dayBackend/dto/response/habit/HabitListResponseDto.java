package day.dayBackend.dto.response.habit;

import lombok.Getter;

import java.util.List;

@Getter
public class HabitListResponseDto {

    private Long totalRows; // 습관 수 
    private Long totalPages; // 페이지 수
    private List<HabitSummaryResponseDto> habitList;

    public static HabitListResponseDto of(long totalRows, long totalPages, List<HabitSummaryResponseDto> habitList) {
        HabitListResponseDto dto = new HabitListResponseDto();
        dto.totalRows = totalRows;
        dto.totalPages = totalPages;
        dto.habitList = habitList;

        return dto;
    }

}
