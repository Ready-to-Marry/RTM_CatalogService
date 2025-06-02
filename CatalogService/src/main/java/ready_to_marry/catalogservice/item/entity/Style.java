package ready_to_marry.catalogservice.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long styleId;

    private Long itemId;
    private String style;
}
