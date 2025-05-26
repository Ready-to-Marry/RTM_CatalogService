package ready_to_marry.catalogservice.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ready_to_marry.catalogservice.common.exception.ErrorCode;
import ready_to_marry.catalogservice.common.exception.InfrastructureException;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.common.util.S3Uploader;
import ready_to_marry.catalogservice.detail.entity.*;
import ready_to_marry.catalogservice.detail.repository.*;
import ready_to_marry.catalogservice.detail.service.DetailService;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.dto.request.ItemUpdateRequest;
import ready_to_marry.catalogservice.item.dto.response.*;
import ready_to_marry.catalogservice.item.entity.Item;
import ready_to_marry.catalogservice.item.entity.Style;
import ready_to_marry.catalogservice.item.entity.Tag;
import ready_to_marry.catalogservice.item.repository.ItemRepository;
import ready_to_marry.catalogservice.item.repository.StyleRepository;
import ready_to_marry.catalogservice.item.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StyleRepository styleRepository;
    private final TagRepository tagRepository;
    private final List<DetailService> detailServices;
    private final WeddingHallRepository weddingHallRepository;
    private final StudioRepository studioRepository;
    private final DressRepository dressRepository;
    private final MakeupRepository makeupRepository;
    private final BouquetRepository bouquetRepository;
    private final InvitationRepository invitationRepository;
    private final VideoRepository videoRepository;
    private final ItemKafkaProducer itemKafkaProducer;
    private final S3Uploader s3Uploader;

    public Long register(Long partnerId, ItemRegisterRequest request, MultipartFile thumbnail, MultipartFile descriptionImage) {
        try {
            Item item = Item.builder()
                    .partnerId(partnerId)
                    .category(request.getCategory())
                    .field(request.getField())
                    .name(request.getName())
                    .region(request.getRegion())
                    .price(request.getPrice())
                    .build();
            itemRepository.save(item);

            Long itemId = item.getItemId();
            String thumbnailKey = String.format("items/thumbnails/item-%d.jpg", itemId);
            String descriptionKey = String.format("items/details/item-%d-desc.jpg", itemId);
            String thumbnailUrl = s3Uploader.uploadWithKey(thumbnail, thumbnailKey);
            String descriptionUrl = s3Uploader.uploadWithKey(descriptionImage, descriptionKey);
            item.setThumbnailUrl(thumbnailUrl);

            styleRepository.saveAll(request.getStyles().stream().map(style -> new Style(null, itemId, style)).toList());
            tagRepository.saveAll(request.getTags().stream().map(tag -> new Tag(null, itemId, tag)).toList());

            switch (request.getField()) {
                case WEDDING_HALL -> weddingHallRepository.save(WeddingHall.builder().item(item).address(request.getAddress()).mealPrice(request.getMealPrice()).capacity(request.getCapacity()).parkingCapacity(request.getParkingCapacity()).descriptionImageUrl(descriptionUrl).build());
                case STUDIO -> studioRepository.save(Studio.builder().item(item).address(request.getAddress()).description(request.getDescription()).descriptionImageUrl(descriptionUrl).build());
                case DRESS -> dressRepository.save(Dress.builder().item(item).address(request.getAddress()).description(request.getDescription()).descriptionImageUrl(descriptionUrl).build());
                case MAKEUP -> makeupRepository.save(Makeup.builder().item(item).address(request.getAddress()).description(request.getDescription()).descriptionImageUrl(descriptionUrl).build());
                case BOUQUET -> bouquetRepository.save(Bouquet.builder().item(item).address(request.getAddress()).description(request.getDescription()).descriptionImageUrl(descriptionUrl).build());
                case INVITATION -> invitationRepository.save(Invitation.builder().item(item).description(request.getDescription()).descriptionImageUrl(descriptionUrl).duration(request.getDuration()).build());
                case VIDEO -> videoRepository.save(Video.builder().item(item).address(request.getAddress()).description(request.getDescription()).descriptionImageUrl(descriptionUrl).build());
                default -> throw new IllegalArgumentException("Unknown category: " + request.getCategory());
            }

            itemKafkaProducer.sendItem("items", ItemKafkaDTO.builder()
                    .operation("create")
                    .itemId(itemId)
                    .partnerId(partnerId)
                    .category(item.getCategory().name())
                    .field(item.getField().name())
                    .name(item.getName())
                    .created_at(item.getCreatedAt().toString())
                    .region(item.getRegion())
                    .price(item.getPrice())
                    .thumbnail_url(thumbnailUrl)
                    .tags(request.getTags())
                    .styles(request.getStyles())
                    .build());

            return itemId;
        } catch (Exception e) {
            throw new InfrastructureException(ErrorCode.DB_WRITE_FAILURE, e);
        }
    }

    public void update(Long itemId, ItemUpdateRequest request, MultipartFile thumbnail, MultipartFile descriptionImage) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        try {
            item.setName(request.getName());
            item.setPrice(request.getPrice());
            item.setRegion(request.getRegion());

            if (thumbnail != null && !thumbnail.isEmpty()) {
                String thumbnailKey = String.format("items/thumbnails/item-%d.jpg", itemId);
                String thumbnailUrl = s3Uploader.uploadWithKey(thumbnail, thumbnailKey);
                item.setThumbnailUrl(thumbnailUrl);
            }

            tagRepository.deleteByItemId(itemId);
            styleRepository.deleteByItemId(itemId);

            tagRepository.saveAll(request.getTags().stream().map(tag -> new Tag(null, itemId, tag)).toList());
            styleRepository.saveAll(request.getStyles().stream().map(style -> new Style(null, itemId, style)).toList());

            switch (item.getField()) {
                case WEDDING_HALL -> weddingHallRepository.findById(itemId).ifPresent(hall -> {
                    hall.setAddress(request.getAddress());
                    hall.setMealPrice(request.getMealPrice());
                    hall.setCapacity(request.getCapacity());
                    hall.setParkingCapacity(request.getParkingCapacity());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        hall.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
                case STUDIO -> studioRepository.findById(itemId).ifPresent(studio -> {
                    studio.setAddress(request.getAddress());
                    studio.setDescription(request.getDescription());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        studio.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
                case DRESS -> dressRepository.findById(itemId).ifPresent(dress -> {
                    dress.setAddress(request.getAddress());
                    dress.setDescription(request.getDescription());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        dress.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
                case MAKEUP -> makeupRepository.findById(itemId).ifPresent(makeup -> {
                    makeup.setAddress(request.getAddress());
                    makeup.setDescription(request.getDescription());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        makeup.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
                case BOUQUET -> bouquetRepository.findById(itemId).ifPresent(bouquet -> {
                    bouquet.setAddress(request.getAddress());
                    bouquet.setDescription(request.getDescription());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        bouquet.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
                case INVITATION -> invitationRepository.findById(itemId).ifPresent(invite -> {
                    invite.setDescription(request.getDescription());
                    invite.setDuration(request.getDuration());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        invite.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
                case VIDEO -> videoRepository.findById(itemId).ifPresent(video -> {
                    video.setAddress(request.getAddress());
                    video.setDescription(request.getDescription());
                    if (descriptionImage != null && !descriptionImage.isEmpty()) {
                        String key = String.format("items/details/item-%d-desc.jpg", itemId);
                        video.setDescriptionImageUrl(s3Uploader.uploadWithKey(descriptionImage, key));
                    }
                });
            }
        } catch (Exception e) {
            throw new InfrastructureException(ErrorCode.DB_WRITE_FAILURE, e);
        }
    }

    public void delete(List<Long> itemIds) {
        try {
            for (Long itemId : itemIds) {
                Item item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

                switch (item.getField()) {
                    case WEDDING_HALL -> weddingHallRepository.deleteById(itemId);
                    case STUDIO -> studioRepository.deleteById(itemId);
                    case DRESS -> dressRepository.deleteById(itemId);
                    case MAKEUP -> makeupRepository.deleteById(itemId);
                    case BOUQUET -> bouquetRepository.deleteById(itemId);
                    case INVITATION -> invitationRepository.deleteById(itemId);
                    case VIDEO -> videoRepository.deleteById(itemId);
                }
            }
            tagRepository.deleteByItemIdIn(itemIds);
            styleRepository.deleteByItemIdIn(itemIds);
            itemRepository.deleteAllById(itemIds);
            itemKafkaProducer.sendItem("items", ItemKafkaDTO.builder().operation("delete").itemIds(itemIds).build());
        } catch (Exception e) {
            throw new InfrastructureException(ErrorCode.DB_WRITE_FAILURE, e);
        }
    }

    public ItemDetailResponse getItemDetails(Long itemId) {
        return this.detailById(itemId);
    }

    public ItemDetailResponse detailById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        DetailService service = detailServices.stream()
                .filter(d -> d.getField().equals(item.getField()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No detail handler for: " + item.getField()));

        List<String> tags = tagRepository.findByItemId(itemId).stream().map(Tag::getTag).toList();
        List<String> styles = styleRepository.findByItemId(itemId).stream().map(Style::getStyle).toList();

        return service.toResponse(item, styles, tags);
    }

    public ItemListResponse listByPartner(Long partnerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("itemId").descending());
        Page<Item> itemPage = itemRepository.findByPartnerId(partnerId, pageable);

        List<ItemDTO> items = itemPage.stream().map(item -> {
            ItemDTO dto = new ItemDTO();
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

        return new ItemListResponse(itemPage.getTotalElements(), page, size, items);
    }
}
