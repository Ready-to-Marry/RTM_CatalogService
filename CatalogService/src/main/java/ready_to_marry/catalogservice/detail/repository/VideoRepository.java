package ready_to_marry.catalogservice.detail.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ready_to_marry.catalogservice.detail.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {}