package ready_to_marry.catalogservice.detail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.Studio;
import ready_to_marry.catalogservice.detail.repository.StudioRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioDetailService implements DetailService {
    private final StudioRepository studioRepository;

    @Override
    public String getCategory() {
        return "studio";
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        Studio studio = studioRepository.findById(item.getItemId())
                .orElseThrow(() -> new NotFoundException("Studio not found: " + item.getItemId()));

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
                .address(studio.getAddress())
                .description(studio.getDescription())
                .descriptionImageUrl(studio.getDescriptionImageUrl())
                .build();
    }
}