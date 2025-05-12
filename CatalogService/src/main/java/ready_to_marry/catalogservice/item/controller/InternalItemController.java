package ready_to_marry.catalogservice.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
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
}
