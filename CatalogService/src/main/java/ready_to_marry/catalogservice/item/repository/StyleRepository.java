package ready_to_marry.catalogservice.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ready_to_marry.catalogservice.item.entity.Style;

import java.util.List;

public interface StyleRepository extends JpaRepository<Style, Long> {

    List<Style> findByItemId(Long itemId);

    @Transactional
    void deleteByItemId(Long itemId);

    @Transactional
    void deleteByItemIdIn(List<Long> itemIds);
}
