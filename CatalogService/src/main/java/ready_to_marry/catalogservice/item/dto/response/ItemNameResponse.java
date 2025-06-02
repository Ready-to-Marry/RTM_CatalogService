package ready_to_marry.catalogservice.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemNameResponse {
    private Long itemId;
    private String name;
}