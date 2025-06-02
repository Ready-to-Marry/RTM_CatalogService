package ready_to_marry.catalogservice.detail.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.Makeup;
import ready_to_marry.catalogservice.detail.repository.MakeupRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;
import ready_to_marry.catalogservice.item.enums.FieldType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MakeupDetailService implements DetailService {
    private final MakeupRepository repo;

    @Override public FieldType getField() {
        return FieldType.MAKEUP;
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        Makeup data = repo.findById(item.getItemId()).orElseThrow(() -> new NotFoundException("Makeup not found"));
        return ItemDetailResponse.builder()
                .itemId(item.getItemId())
                .category(item.getCategory())
                .field(item.getField())
                .name(item.getName())
                .region(item.getRegion())
                .price(item.getPrice())
                .thumbnailUrl(item.getThumbnailUrl())
                .styles(styles).tags(tags)
                .address(data.getAddress())
                .description(data.getDescription())
                .descriptionImageUrl(data.getDescriptionImageUrl())
                .build();
    }
}