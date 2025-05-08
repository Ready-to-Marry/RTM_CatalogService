package ready_to_marry.catalogservice.detail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.Dress;
import ready_to_marry.catalogservice.detail.repository.DressRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DressDetailService implements DetailService {
    private final DressRepository dressRepository;

    @Override
    public String getCategory() {
        return "dress";
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        Dress dress = dressRepository.findById(item.getItemId())
                .orElseThrow(() -> new NotFoundException("Dress not found: " + item.getItemId()));

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
                .address(dress.getAddress())
                .description(dress.getDescription())
                .descriptionImageUrl(dress.getDescriptionImageUrl())
                .build();
    }
}