package ready_to_marry.catalogservice.item.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemNameRequest {
    private List<Long> itemIds;
}