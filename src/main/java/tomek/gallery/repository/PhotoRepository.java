package tomek.gallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomek.gallery.entity.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByGalleryId(Long galleryId);
}
