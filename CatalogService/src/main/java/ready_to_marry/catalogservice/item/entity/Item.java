package ready_to_marry.catalogservice.item.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private Long partnerId;
    private String category;
    private String field;
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private final LocalDateTime createdAt = LocalDateTime.now();
}