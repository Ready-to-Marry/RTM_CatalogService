package ready_to_marry.catalogservice.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ready_to_marry.catalogservice.item.dto.request.ItemRegisterRequest;
import ready_to_marry.catalogservice.item.entity.Item;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemKafkaDto {
    private String itemId;
    private String partnerId;
    private String category;
    private String field;
    private String name;
    private String created_at;
    private String region;
    private Long price;
    private String thumbnail_url;
    private List<String> tags;
    private List<String> styles;

    public static ItemKafkaDto from(Item item, ItemRegisterRequest request) {
        return ItemKafkaDto.builder()
                .itemId(String.valueOf(item.getItemId()))
                .partnerId(String.valueOf(item.getPartnerId()))
                .category(item.getCategory().name())
                .field(item.getField().name())
                .name(item.getName())
                .created_at(item.getCreatedAt().toString())
                .region(item.getRegion())
                .price(item.getPrice())
                .thumbnail_url(item.getThumbnailUrl())
                .tags(request.getTags())
                .styles(request.getStyles())
                .build();
    }
}