package ready_to_marry.catalogservice.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.common.exception.NotFoundException;
import ready_to_marry.catalogservice.item.dto.request.ItemNameRequest;
import ready_to_marry.catalogservice.item.dto.response.ItemNameResponse;
import ready_to_marry.catalogservice.item.repository.ItemRepository;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class InternalItemController {

    private final ItemRepository itemRepository;

    // User 리뷰 조회 시 itemName(리스트) 조회
    @PostMapping("/names")
    public ApiResponse<List<ItemNameResponse>> getItemNames(@RequestBody @Valid ItemNameRequest request) {
        if (request.getItemIds().isEmpty()) {
            return ApiResponse.success(List.of());
        }

        List<ItemNameResponse> result = itemRepository
                .findAllById(request.getItemIds()).stream()
                .map(item -> new ItemNameResponse(item.getItemId(), item.getName()))
                .toList();

        return ApiResponse.success(result);
    }

    // item_id에 해당하는 partner_id 반환
    @GetMapping("/{itemId}/partner-id")
    public ApiResponse<Long> getPartnerIdByItemId(@PathVariable Long itemId) {
        Long partnerId = itemRepository.findById(itemId)
                .map(item -> item.getPartnerId())
                .orElseThrow(() -> new NotFoundException("해당 item이 존재하지 않습니다."));
        return ApiResponse.success(partnerId);
    }
}
