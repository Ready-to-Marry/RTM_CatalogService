package ready_to_marry.catalogservice.item.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import ready_to_marry.catalogservice.item.enums.CategoryType;
import ready_to_marry.catalogservice.item.enums.FieldType;

import java.util.List;

@Builder
@Getter
public class ItemRegisterRequest {

    @NotNull(message = "카테고리는 필수입니다.")
    private CategoryType category;

    @NotNull(message = "세부 필드는 필수입니다.")
    private FieldType field;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "지역 정보는 필수입니다.")
    private String region;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long price;

    // 이미지 URL → 이미지 파일로 대체 (S3 업로드로 설정됨)
    private String thumbnailUrl;
    private String descriptionImageUrl;

    @NotNull(message = "태그 목록은 필수입니다.")
    @Size(min = 1, message = "태그는 최소 1개 이상이어야 합니다.")
    private List<String> tags;

    @NotNull(message = "스타일 목록은 필수입니다.")
    @Size(min = 1, message = "스타일은 최소 1개 이상이어야 합니다.")
    private List<String> styles;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @NotBlank(message = "상세 설명은 필수입니다.")
    private String description;

    // 웨딩홀 전용
    private Integer mealPrice;
    private Integer capacity;
    private Integer parkingCapacity;

    // 영상/청첩장 전용
    private Integer duration;

    @AssertTrue(message = "웨딩홀 등록 시 식대, 수용 인원, 주차 공간은 필수입니다.")
    public boolean isValidForWeddingHall() {
        if (field == FieldType.WEDDING_HALL) {
            return mealPrice != null && capacity != null && parkingCapacity != null;
        }
        return true;
    }

    @AssertTrue(message = "영상/청첩장 등록 시 예상 배송 기간은 필수입니다.")
    public boolean isValidForVideoOrInvitation() {
        if (field == FieldType.VIDEO || field == FieldType.INVITATION) {
            return duration != null && duration >= 0;
        }
        return true;
    }
}
