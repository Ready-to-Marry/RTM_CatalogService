package ready_to_marry.catalogservice.item.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import ready_to_marry.catalogservice.item.enums.FieldType;

import java.util.List;

@Getter
public class ItemUpdateRequest {

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "지역 정보는 필수입니다.")
    private String region;

    // 가격 문의 허용
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long price;

    @NotBlank(message = "썸네일 이미지 URL은 필수입니다.")
    private String thumbnailUrl;

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

    @NotBlank(message = "설명 이미지 URL은 필수입니다.")
    private String descriptionImageUrl;

    // 카테고리 기반 조건부 필수 필드
    private Integer mealPrice;
    private Integer capacity;
    private Integer parkingCapacity;
    private Integer duration;

    @NotNull(message = "세부 필드는 필수입니다.")
    private FieldType field;

    // ✅ 웨딩홀 조건부 검증
    @AssertTrue(message = "웨딩홀 수정 시 식대, 수용 인원, 주차 공간은 필수입니다.")
    public boolean isValidForWeddingHall() {
        if (field == FieldType.WEDDING_HALL) {
            return mealPrice != null && capacity != null && parkingCapacity != null;
        }
        return true;
    }

    // ✅ 영상/청첩장 조건부 검증
    @AssertTrue(message = "영상/청첩장 등록 시 예상 배송 기간은 필수입니다.")
    public boolean isValidForVideoOrInvitation() {
        if (field == FieldType.VIDEO || field == FieldType.INVITATION) {
            return duration != null && duration >= 0;
        }
        return true;
    }
}
