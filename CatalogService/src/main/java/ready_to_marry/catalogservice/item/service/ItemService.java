package ready_to_marry.catalogservice.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.ErrorCode;
import ready_to_marry.catalogservice.common.exception.InfrastructureException;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.*;
import ready_to_marry.catalogservice.detail.repository.*;
import ready_to_marry.catalogservice.detail.service.DetailService;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.dto.request.ItemUpdateRequest;
import ready_to_marry.catalogservice.item.dto.response.ItemDTO;
import ready_to_marry.catalogservice.item.dto.response.ItemKafkaDto;
import ready_to_marry.catalogservice.item.dto.response.ItemListResponse;
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

    public Long register(Long partnerId, ItemRegisterRequest request) {
        try {
            Item item = Item.builder()
                    .partnerId(partnerId)
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

            switch (request.getField()) {
                case WEDDING_HALL -> weddingHallRepository.save(
                        WeddingHall.builder()
                                .item(item)
                                .address(request.getAddress())
                                .mealPrice(request.getMealPrice())
                                .capacity(request.getCapacity())
                                .parkingCapacity(request.getParkingCapacity())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .build()
                );
                case STUDIO -> studioRepository.save(
                        Studio.builder()
                                .item(item)
                                .address(request.getAddress())
                                .description(request.getDescription())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .build()
                );
                case DRESS -> dressRepository.save(
                        Dress.builder()
                                .item(item)
                                .address(request.getAddress())
                                .description(request.getDescription())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .build()
                );
                case MAKEUP -> makeupRepository.save(
                        Makeup.builder()
                                .item(item)
                                .address(request.getAddress())
                                .description(request.getDescription())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .build()
                );
                case BOUQUET -> bouquetRepository.save(
                        Bouquet.builder()
                                .item(item)
                                .address(request.getAddress())
                                .description(request.getDescription())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .build()
                );
                case INVITATION -> invitationRepository.save(
                        Invitation.builder()
                                .item(item)
                                .description(request.getDescription())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .duration(request.getDuration())
                                .build()
                );
                case VIDEO -> videoRepository.save(
                        Video.builder()
                                .item(item)
                                .address(request.getAddress())
                                .description(request.getDescription())
                                .descriptionImageUrl(request.getDescriptionImageUrl())
                                .build()
                );
                default -> throw new IllegalArgumentException("Unknown category: " + request.getCategory());
            }

            itemKafkaProducer.sendItem("items", ItemKafkaDto.from(item, request));

            return item.getItemId();
        } catch (Exception e) {
            throw new InfrastructureException(ErrorCode.DB_WRITE_FAILURE, e);
        }
    }

    public void update(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        try {
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

            switch (item.getField()) {
                case WEDDING_HALL -> weddingHallRepository.findById(itemId).ifPresent(hall -> {
                    hall.setAddress(request.getAddress());
                    hall.setMealPrice(request.getMealPrice());
                    hall.setCapacity(request.getCapacity());
                    hall.setParkingCapacity(request.getParkingCapacity());
                    hall.setDescriptionImageUrl(request.getDescriptionImageUrl());
                });
                case STUDIO -> studioRepository.findById(itemId).ifPresent(studio -> {
                    studio.setAddress(request.getAddress());
                    studio.setDescription(request.getDescription());
                    studio.setDescriptionImageUrl(request.getDescriptionImageUrl());
                });
                case DRESS -> dressRepository.findById(itemId).ifPresent(dress -> {
                    dress.setAddress(request.getAddress());
                    dress.setDescription(request.getDescription());
                    dress.setDescriptionImageUrl(request.getDescriptionImageUrl());
                });
                case MAKEUP -> makeupRepository.findById(itemId).ifPresent(makeup -> {
                    makeup.setAddress(request.getAddress());
                    makeup.setDescription(request.getDescription());
                    makeup.setDescriptionImageUrl(request.getDescriptionImageUrl());
                });
                case BOUQUET -> bouquetRepository.findById(itemId).ifPresent(bouquet -> {
                    bouquet.setAddress(request.getAddress());
                    bouquet.setDescription(request.getDescription());
                    bouquet.setDescriptionImageUrl(request.getDescriptionImageUrl());
                });
                case INVITATION -> invitationRepository.findById(itemId).ifPresent(invite -> {
                    invite.setDescription(request.getDescription());
                    invite.setDescriptionImageUrl(request.getDescriptionImageUrl());
                    invite.setDuration(request.getDuration());
                });
                case VIDEO -> videoRepository.findById(itemId).ifPresent(video -> {
                    video.setAddress(request.getAddress());
                    video.setDescription(request.getDescription());
                    video.setDescriptionImageUrl(request.getDescriptionImageUrl());
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
                    default -> throw new IllegalArgumentException("Unknown field: " + item.getField());
                }
            }

            tagRepository.deleteByItemIdIn(itemIds);
            styleRepository.deleteByItemIdIn(itemIds);
            itemRepository.deleteAllById(itemIds);
        } catch (Exception e) {
            throw new InfrastructureException(ErrorCode.DB_WRITE_FAILURE, e);
        }
    }

    public ItemDetailResponse getItemDetails(Long itemId) {
        return this.detailById(itemId);
    }
}
