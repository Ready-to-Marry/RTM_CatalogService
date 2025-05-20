package ready_to_marry.catalogservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 16x: 비즈니스 오류

    // 2xxx: 인프라(시스템) 오류
    DB_WRITE_FAILURE(2600, "Failed to write data to the database"),
    KAFKA_SERIALIZATION_ERROR(2601, "Failed to serialization message to Kafka"),
    KAFKA_CONNECTION_ERROR(2602, "Failed to connect to Kafka broker"),
    UNKNOWN_ERROR(2603, "Unknown error");

    private final int code;
    private final String message;
}