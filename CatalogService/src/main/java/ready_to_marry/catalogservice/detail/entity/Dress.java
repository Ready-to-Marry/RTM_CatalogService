package ready_to_marry.catalogservice.detail.entity;

import jakarta.persistence.*;
import lombok.*;
import ready_to_marry.catalogservice.item.entity.Item;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dress {
    @Id
    private Long itemId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "item_id")
    private Item item;

    private String address;
    private String description;
    private String descriptionImageUrl;
}