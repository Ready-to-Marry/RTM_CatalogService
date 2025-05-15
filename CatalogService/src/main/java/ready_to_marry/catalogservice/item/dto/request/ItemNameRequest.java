package ready_to_marry.catalogservice.item.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemNameRequest {
    @NotNull(message = "itemIds는 null일 수 없습니다.")
    private List<Long> itemIds;
}