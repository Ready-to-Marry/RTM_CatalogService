package ready_to_marry.catalogservice.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.service.DetailService;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternalItemService {
    private final ItemRepository itemRepository;
    private final StyleRepository styleRepository;
    private final TagRepository tagRepository;
    private final List<DetailService> detailServices;

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

    public ItemDetailResponse detailById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        DetailService service = detailServices.stream()
                .filter(d -> d.getCategory().equals(item.getCategory()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No detail handler for: " + item.getCategory()));

        List<String> tags = tagRepository.findByItemId(itemId).stream().map(Tag::getTag).toList();
        List<String> styles = styleRepository.findByItemId(itemId).stream().map(Style::getStyle).toList();

        return service.toResponse(item, styles, tags);
    }
}