package ready_to_marry.catalogservice.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;

@Service
@RequiredArgsConstructor
public class PublicItemService {
    private final InternalItemService internalItemService;

    public ItemDetailResponse getItemDetails(Long itemId) {
        return internalItemService.detailById(itemId);
    }
}