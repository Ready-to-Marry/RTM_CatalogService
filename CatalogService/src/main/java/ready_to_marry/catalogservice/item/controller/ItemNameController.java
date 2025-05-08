package ready_to_marry.catalogservice.item.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.item.dto.request.ItemNameRequest;
import ready_to_marry.catalogservice.item.dto.response.ItemNameResponse;
import ready_to_marry.catalogservice.item.repository.ItemRepository;

import java.util.List;

@RestController
@RequestMapping("/catalog-service/internal/items")
@RequiredArgsConstructor
public class ItemNameController {

    private final ItemRepository itemRepository;

    @PostMapping("/names")
    public ApiResponse<List<ItemNameResponse>> getItemNames(@RequestBody ItemNameRequest request) {
        List<ItemNameResponse> result;
        result = itemRepository
                .findAllById(request.getItemIds()).stream()
                .map(item -> new ItemNameResponse(item.getItemId(), item.getName()))
                .toList();

        return ApiResponse.success(result);
    }
}
