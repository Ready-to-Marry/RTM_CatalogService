package ready_to_marry.catalogservice.detail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ready_to_marry.catalogservice.detail.entity.Makeup;

public interface MakeupRepository extends JpaRepository<Makeup, Long> {}