package ready_to_marry.catalogservice.common.dto;

public class ErrorDetail {
    private String field;
    private String reason;

    public ErrorDetail(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    public String getField() { return field; }
    public String getReason() { return reason; }
    public void setField(String field) { this.field = field; }
    public void setReason(String reason) { this.reason = reason; }
}