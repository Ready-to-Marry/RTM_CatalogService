package ready_to_marry.catalogservice.item.dto.request;

import lombok.Getter;
import java.util.List;
import ready_to_marry.catalogservice.item.enums.FieldType;

@Getter
public class ItemUpdateRequest {
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private List<String> styles;
    private List<String> tags;
}
