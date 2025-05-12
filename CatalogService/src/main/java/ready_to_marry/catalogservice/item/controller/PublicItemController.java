package ready_to_marry.catalogservice.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.service.PublicItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class PublicItemController {
    private final PublicItemService service;

    // 상세 조회 => User, Admin, Partner
    @GetMapping("/{itemId}/details")
    public ApiResponse<ItemDetailResponse> detail(@PathVariable Long itemId) {
        return ApiResponse.success(service.getItemDetails(itemId));
    }
}