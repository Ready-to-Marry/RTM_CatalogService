package ready_to_marry.catalogservice.detail.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.Bouquet;
import ready_to_marry.catalogservice.detail.repository.BouquetRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BouquetDetailService implements DetailService {
    private final BouquetRepository bouquetRepository;

    @Override
    public String getCategory() {
        return "bouquet";
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        Bouquet bouquet = bouquetRepository.findById(item.getItemId())
                .orElseThrow(() -> new NotFoundException("Bouquet not found: " + item.getItemId()));

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
                .address(bouquet.getAddress())
                .description(bouquet.getDescription())
                .descriptionImageUrl(bouquet.getDescriptionImageUrl())
                .build();
    }
}