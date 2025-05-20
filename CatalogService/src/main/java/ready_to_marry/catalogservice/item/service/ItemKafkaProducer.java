package ready_to_marry.catalogservice.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ready_to_marry.catalogservice.common.exception.ErrorCode;
import ready_to_marry.catalogservice.common.exception.InfrastructureException;
import ready_to_marry.catalogservice.item.dto.response.ItemKafkaDTO;

@RequiredArgsConstructor
@Service
public class ItemKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendItem(String topic, ItemKafkaDTO dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            throw new InfrastructureException(ErrorCode.KAFKA_PUBLISH_FAILURE, e);
        } catch (TimeoutException e) {
            throw new InfrastructureException(ErrorCode.KAFKA_CONNECTION_ERROR, e);
        }
    }
}
