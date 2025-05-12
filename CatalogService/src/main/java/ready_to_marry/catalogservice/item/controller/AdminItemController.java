package ready_to_marry.catalogservice.item.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.common.dto.Meta;
import ready_to_marry.catalogservice.item.dto.response.ItemDTO;
import ready_to_marry.catalogservice.item.dto.response.ItemListResponse;
import ready_to_marry.catalogservice.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items/admin")
@RequiredArgsConstructor
public class AdminItemController {
    private final ItemService service;

    // Admin -> 특정 partnerId의 item 목록 조회
    @GetMapping
    public ApiResponse<List<ItemDTO>> list(
            @RequestParam Long partnerId, //
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        ItemListResponse resp = service.listByPartner(partnerId, page, size);
        Meta meta = new Meta(resp.getPage(), resp.getSize(), resp.getTotal(), (int)Math.ceil((double)resp.getTotal()/resp.getSize()));
        return ApiResponse.success(resp.getItems(), meta);
    }

}