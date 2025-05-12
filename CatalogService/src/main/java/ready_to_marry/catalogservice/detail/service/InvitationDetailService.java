package ready_to_marry.catalogservice.detail.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.detail.entity.Invitation;
import ready_to_marry.catalogservice.detail.repository.InvitationRepository;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.entity.Item;
import ready_to_marry.catalogservice.item.enums.FieldType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationDetailService implements DetailService {
    private final InvitationRepository invitationRepository;

    @Override
    public FieldType getField() {
        return FieldType.INVITATION;
    }

    @Override
    public ItemDetailResponse toResponse(Item item, List<String> styles, List<String> tags) {
        Invitation invitation = invitationRepository.findById(item.getItemId())
                .orElseThrow(() -> new NotFoundException("Invitation not found: " + item.getItemId()));

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
                .description(invitation.getDescription())
                .descriptionImageUrl(invitation.getDescriptionImageUrl())
                .videoOrInvitationDetail(
                        ItemDetailResponse.VideoOrInvitationDetail.builder()
                                .duration(invitation.getDuration())
                                .build()
                )
                .build();

    }
}