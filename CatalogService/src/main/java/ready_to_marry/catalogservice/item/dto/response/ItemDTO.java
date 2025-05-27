package ready_to_marry.catalogservice.item.dto.response;

import lombok.Getter;
import lombok.Setter;
import ready_to_marry.catalogservice.item.enums.CategoryType;
import ready_to_marry.catalogservice.item.enums.FieldType;

import java.util.List;

@Getter
@Setter
public class ItemDTO {
    private Long itemId;
    private CategoryType category;
    private FieldType field;
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private List<String> tags;
    private List<String> styles;
}