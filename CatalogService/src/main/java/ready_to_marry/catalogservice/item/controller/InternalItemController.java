package ready_to_marry.catalogservice.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.common.dto.Meta;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.dto.request.ItemUpdateRequest;
import ready_to_marry.catalogservice.item.dto.response.InternalItemDTO;
import ready_to_marry.catalogservice.item.dto.response.InternalItemListResponse;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.service.InternalItemService;

import java.util.List;

@RestController
@RequestMapping("/catalog-service/internal/items")
@RequiredArgsConstructor
public class InternalItemController {

    private final InternalItemService service;

    // 1. 상세 조회
    @GetMapping("/{itemId}/details")
    public ApiResponse<ItemDetailResponse> detail(@PathVariable Long itemId) {
        return ApiResponse.success(service.detailById(itemId));
    }

    // 2. 목록 조회
    @GetMapping
    public ApiResponse<List<InternalItemDTO>> listByPartner(
            @RequestParam Long partnerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        InternalItemListResponse result = service.listByPartner(partnerId, page, size);
        Meta meta = new Meta(result.getPage(), result.getSize(), result.getTotal(), (int) Math.ceil((double) result.getTotal() / result.getSize()));
        return ApiResponse.success(result.getItems(), meta);
    }

    // 3. 등록
    @PostMapping
    public ApiResponse<Long> register(@RequestBody ItemRegisterRequest request) {
        Long itemId = service.register(request);
        return ApiResponse.success(itemId);
    }

    // 4. 수정
    @PatchMapping("/{itemId}")
    public ApiResponse<?> update(@PathVariable Long itemId, @RequestBody ItemUpdateRequest request) {
        service.update(itemId, request);
        return ApiResponse.success(null);
    }

    // 5. 삭제
    @DeleteMapping
    public ApiResponse<?> delete(@RequestBody List<Long> itemIds) {
        service.delete(itemIds);
        return ApiResponse.success(null);
    }
}
