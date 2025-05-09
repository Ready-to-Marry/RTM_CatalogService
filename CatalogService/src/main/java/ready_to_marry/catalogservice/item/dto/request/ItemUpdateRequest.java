package ready_to_marry.catalogservice.item.dto.request;

import lombok.Getter;
import java.util.List;

@Getter
public class ItemUpdateRequest {
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private List<String> styles;
    private List<String> tags;

    // 카테고리별 공통 상세 필드
    private String address;
    private String description;
    private String descriptionImageUrl;

    // 웨딩홀 전용
    private Integer mealPrice;
    private Integer capacity;
    private Integer parkingCapacity;

    // 영상/청첩장 전용
    private Integer duration;
}
