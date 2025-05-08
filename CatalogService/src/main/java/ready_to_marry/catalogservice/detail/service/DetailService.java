package ready_to_marry.catalogservice.detail.service;


import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;

import java.util.List;

/**
 * 카테고리별 상세 조회를 위한 공통 인터페이스
 */
public interface DetailService {

    /**
     * 이 서비스가 처리할 카테고리명을 반환합니다.
     * 예: "wedding_hall", "studio", "dress", ...
     */
    String getCategory();

    /**
     * 공통 Item 엔티티 + 스타일/태그 리스트 → 카테고리별 상세 DTO로 매핑합니다.
     *
     * @param item        Item 엔티티 (공통 정보)
     * @param styles      스타일 목록
     * @param tags        태그 목록
     * @return ItemDetailResponse DTO
     */
    ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags);
}
