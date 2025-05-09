package ready_to_marry.catalogservice.detail.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.Video;
import ready_to_marry.catalogservice.detail.repository.VideoRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoDetailService implements DetailService {
    private final VideoRepository videoRepository;

    @Override
    public String getCategory() {
        return "video";
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        Video video = videoRepository.findById(item.getItemId())
                .orElseThrow(() -> new NotFoundException("Video not found: " + item.getItemId()));

        return ItemDetailResponse.builder()
                .itemId(item.getItemId())
                .category(item.getCategory())
                .field(item.getField())
                .name(item.getName())
                .region(video.getAddress())
                .price(item.getPrice())
                .thumbnailUrl(item.getThumbnailUrl())
                .styles(styles)
                .tags(tags)
                .description(video.getDescription())
                .descriptionImageUrl(video.getDescriptionImageUrl())
                .build();
    }
}