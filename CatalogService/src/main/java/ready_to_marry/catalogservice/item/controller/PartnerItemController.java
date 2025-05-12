package ready_to_marry.catalogservice.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.dto.request.ItemUpdateRequest;
import ready_to_marry.catalogservice.item.dto.response.ItemListResponse;
import ready_to_marry.catalogservice.item.service.InternalItemService;

import java.util.List;

@RestController
@RequestMapping("/items/partners")
@RequiredArgsConstructor
public class PartnerItemController {

    private final InternalItemService service;

    // 1. 등록 -> Partner
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> register(
            @RequestHeader("X-Partner-Id") Long partnerId,
            @RequestBody ItemRegisterRequest request) {
        Long itemId = service.register(partnerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(itemId));
    }

    // 2. 수정 -> Partner
    @PatchMapping("/{itemId}")
    public ApiResponse<?> update(@PathVariable Long itemId, @RequestBody ItemUpdateRequest request) {
        service.update(itemId, request);
        return ApiResponse.success(null);
    }

    // 3. 삭제 -> Partner
    @DeleteMapping("/{itemId}")
    public ApiResponse<?> delete(@PathVariable Long itemId) {
        service.delete(List.of(itemId));
        return ApiResponse.success(null);
    }


    // 4. Partner -> Header에서 partnerId로 item 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<ItemListResponse>> getPartnerItemList(
            @RequestHeader("X-Partner-Id") Long partnerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ItemListResponse response = service.listByPartner(partnerId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
