package ready_to_marry.catalogservice.item.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ready_to_marry.catalogservice.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByPartnerId(Long partnerId, Pageable pageable);
}