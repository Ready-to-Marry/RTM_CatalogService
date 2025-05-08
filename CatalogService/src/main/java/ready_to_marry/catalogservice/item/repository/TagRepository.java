package ready_to_marry.catalogservice.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ready_to_marry.catalogservice.item.entity.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByItemId(Long itemId);
}