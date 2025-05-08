package ready_to_marry.catalogservice.item.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class InternalItemDTO {
    private Long itemId;
    private String category;
    private String field;
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private List<String> tags;
    private List<String> styles;
}