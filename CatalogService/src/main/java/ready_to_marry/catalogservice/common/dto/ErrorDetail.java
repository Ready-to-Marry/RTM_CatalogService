package ready_to_marry.catalogservice.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDetail {
    private String field;
    private String reason;

    public ErrorDetail(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

}