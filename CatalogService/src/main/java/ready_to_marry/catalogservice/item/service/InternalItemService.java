package ready_to_marry.catalogservice.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.service.DetailService;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.dto.request.ItemUpdateRequest;
import ready_to_marry.catalogservice.item.dto.response.InternalItemDTO;
import ready_to_marry.catalogservice.item.dto.response.InternalItemListResponse;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;
import ready_to_marry.catalogservice.item.entity.Style;
import ready_to_marry.catalogservice.item.entity.Tag;
import ready_to_marry.catalogservice.item.repository.ItemRepository;
import ready_to_marry.catalogservice.item.repository.StyleRepository;
import ready_to_marry.catalogservice.item.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalItemService {

    private final ItemRepository itemRepository;
    private final StyleRepository styleRepository;
    private final TagRepository tagRepository;
    private final List<DetailService> detailServices;

    // 1. 목록 조회
    public InternalItemListResponse listByPartner(Long partnerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("itemId").descending());
        Page<Item> itemPage = itemRepository.findByPartnerId(partnerId, pageable);

        List<InternalItemDTO> items = itemPage.stream().map(item -> {
            InternalItemDTO dto = new InternalItemDTO();
            dto.setItemId(item.getItemId());
            dto.setCategory(item.getCategory());
            dto.setField(item.getField());
            dto.setName(item.getName());
            dto.setRegion(item.getRegion());
            dto.setPrice(item.getPrice());
            dto.setThumbnailUrl(item.getThumbnailUrl());
            dto.setTags(tagRepository.findByItemId(item.getItemId()).stream().map(Tag::getTag).toList());
            dto.setStyles(styleRepository.findByItemId(item.getItemId()).stream().map(Style::getStyle).toList());
            return dto;
        }).toList();

        return new InternalItemListResponse(itemPage.getTotalElements(), page, size, items);
    }

    // 2. 단건 상세 조회
    public ItemDetailResponse detailById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        DetailService service = detailServices.stream()
                .filter(d -> d.getCategory().equals(item.getCategory()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No detail handler for: " + item.getCategory()));

        List<String> tags = tagRepository.findByItemId(itemId).stream().map(Tag::getTag).toList();
        List<String> styles = styleRepository.findByItemId(itemId).stream().map(Style::getStyle).toList();

        return service.toResponse(item, styles, tags);
    }

    // 3. 등록
    public Long register(ItemRegisterRequest request) {
        Item item = Item.builder()
                .partnerId(request.getPartnerId())
                .category(request.getCategory())
                .field(request.getField())
                .name(request.getName())
                .region(request.getRegion())
                .price(request.getPrice())
                .thumbnailUrl(request.getThumbnailUrl())
                .build();

        itemRepository.save(item);

        List<Style> styles = request.getStyles().stream()
                .map(style -> new Style(null, item.getItemId(), style))
                .toList();
        List<Tag> tags = request.getTags().stream()
                .map(tag -> new Tag(null, item.getItemId(), tag))
                .toList();

        styleRepository.saveAll(styles);
        tagRepository.saveAll(tags);

        return item.getItemId();
    }

    // 4. 수정
    public void update(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setRegion(request.getRegion());
        item.setThumbnailUrl(request.getThumbnailUrl());
        itemRepository.save(item);

        tagRepository.deleteByItemId(itemId);
        styleRepository.deleteByItemId(itemId);

        List<Tag> tags = request.getTags().stream()
                .map(tag -> new Tag(null, itemId, tag))
                .toList();
        List<Style> styles = request.getStyles().stream()
                .map(style -> new Style(null, itemId, style))
                .toList();

        tagRepository.saveAll(tags);
        styleRepository.saveAll(styles);
    }

    // 5. 삭제
    public void delete(List<Long> itemIds) {
        itemRepository.deleteAllById(itemIds);
        tagRepository.deleteByItemIdIn(itemIds);
        styleRepository.deleteByItemIdIn(itemIds);
        // 💡 상세 테이블 삭제도 필요시 여기에 연동
    }
}