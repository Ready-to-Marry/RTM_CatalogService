package ready_to_marry.catalogservice.item.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.catalogservice.common.dto.ApiResponse;
import ready_to_marry.catalogservice.common.dto.Meta;
import ready_to_marry.catalogservice.item.dto.response.InternalItemDTO;
import ready_to_marry.catalogservice.item.dto.response.InternalItemListResponse;
import ready_to_marry.catalogservice.item.service.InternalItemService;

import java.util.List;

@RestController
@RequestMapping("/catalog-service/internal/items")
@RequiredArgsConstructor
public class PartnerItemController {
    private final InternalItemService service;

    @GetMapping
    public ApiResponse<List<InternalItemDTO>> list(
            @RequestParam Long partnerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        InternalItemListResponse resp = service.listByPartner(partnerId, page, size);
        Meta meta = new Meta(resp.getPage(), resp.getSize(), resp.getTotal(), (int)Math.ceil((double)resp.getTotal()/resp.getSize()));
        return ApiResponse.success(resp.getItems(), meta);
    }
}