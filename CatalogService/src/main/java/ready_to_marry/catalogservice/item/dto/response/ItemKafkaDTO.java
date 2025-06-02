package ready_to_marry.catalogservice.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemKafkaDTO {
    private String operation;
    private Long itemId;
    private Long partnerId;
    private String category;
    private String field;
    private String name;
    private String created_at;
    private String region;
    private Long price;
    private String thumbnail_url;
    private List<String> tags;
    private List<String> styles;
    private List<Long> itemIds;
}