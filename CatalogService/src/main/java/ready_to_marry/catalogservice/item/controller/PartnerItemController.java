package ready_to_marry.catalogservice.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.dto.request.ItemUpdateRequest;
import ready_to_marry.catalogservice.item.dto.response.ItemListResponse;
import ready_to_marry.catalogservice.item.dto.response.ItemDetailResponse;
import ready_to_marry.catalogservice.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items/partners")
@RequiredArgsConstructor
public class PartnerItemController {

    private final ItemService service;

    // 1. 등록 -> Partner (with image upload)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> register(
            @RequestHeader("X-Partner-Id") Long partnerId,
            @RequestPart("request") @Valid ItemRegisterRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("descriptionImage") MultipartFile descriptionImage) {
        Long itemId = service.register(partnerId, request, thumbnail, descriptionImage);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(itemId));
    }

    // 2. 수정 -> Partner (with image upload)
    @PatchMapping(value = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> update(@PathVariable Long itemId,
                                 @RequestPart("request") @Valid ItemUpdateRequest request,
                                 @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                 @RequestPart(value = "descriptionImage", required = false) MultipartFile descriptionImage) {
        service.update(itemId, request, thumbnail, descriptionImage);
        return ApiResponse.success(null);
    }

    // 3. 삭제 -> Partner
    @DeleteMapping()
    public ApiResponse<?> delete(@RequestBody List<Long> itemIds) {
        service.delete(itemIds);
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

    // 5. 단건 조회
    @GetMapping("/{itemId}")
    public ApiResponse<ItemDetailResponse> getDetail(@PathVariable Long itemId) {
        return ApiResponse.success(service.getItemDetails(itemId));
    }
}
