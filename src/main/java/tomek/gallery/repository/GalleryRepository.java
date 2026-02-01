package tomek.gallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomek.gallery.entity.Gallery;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByUserId(Long userId);
}
