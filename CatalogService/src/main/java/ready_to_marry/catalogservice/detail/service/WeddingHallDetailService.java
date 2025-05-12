package ready_to_marry.catalogservice.detail.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.WeddingHall;
import ready_to_marry.catalogservice.detail.repository.WeddingHallRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;
import ready_to_marry.catalogservice.item.enums.FieldType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeddingHallDetailService implements DetailService {
    private final WeddingHallRepository weddingHallRepository;

    @Override
    public FieldType getField() {
        return FieldType.WEDDING_HALL;
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        WeddingHall hall = weddingHallRepository.findById(item.getItemId())
                .orElseThrow(() -> new NotFoundException("Wedding hall not found: " + item.getItemId()));

        return ItemDetailResponse.builder()
                .itemId(item.getItemId())
                .category(item.getCategory())
                .field(item.getField())
                .name(item.getName())
                .region(item.getRegion())
                .price(item.getPrice())
                .thumbnailUrl(item.getThumbnailUrl())
                .styles(styles)
                .tags(tags)
                .address(hall.getAddress())
                .descriptionImageUrl(hall.getDescriptionImageUrl())
                .weddingHallDetail(
                        ItemDetailResponse.WeddingHallDetail.builder()
                                .mealPrice(hall.getMealPrice())
                                .capacity(hall.getCapacity())
                                .parkingCapacity(hall.getParkingCapacity())
                                .build()
                )
                .build();
    }
}