package ready_to_marry.catalogservice.item.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.service.InternalItemService;

@RestController
@RequestMapping("/catalog-service/internal/items")
@RequiredArgsConstructor
public class InternalItemController {
    private final InternalItemService service;

    @GetMapping("/{itemId}/details")
    public ApiResponse<ItemDetailResponse> detail(@PathVariable Long itemId) {
        return ApiResponse.success(service.detailById(itemId));
    }
}