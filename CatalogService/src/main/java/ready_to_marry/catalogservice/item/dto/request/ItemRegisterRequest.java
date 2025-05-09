package ready_to_marry.catalogservice.item.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemRegisterRequest {
    private Long partnerId;
    private String category;
    private String field;
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private List<String> styles;
    private List<String> tags;
}
