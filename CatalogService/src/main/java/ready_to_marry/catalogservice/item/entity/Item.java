package ready_to_marry.catalogservice.item.entity;

import jakarta.persistence.*;
import lombok.*;
import ready_to_marry.catalogservice.item.enums.CategoryType;
import ready_to_marry.catalogservice.item.enums.FieldType;

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

    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Enumerated(EnumType.STRING)
    private FieldType field;

    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;

    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();
}
