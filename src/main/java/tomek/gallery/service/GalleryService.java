package tomek.gallery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomek.gallery.dto.GalleryDTO;
import tomek.gallery.entity.Gallery;
import tomek.gallery.entity.User;
import tomek.gallery.repository.GalleryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    public Gallery createGallery(String name, User user) {
        Gallery gallery = new Gallery();
        gallery.setName(name);
        gallery.setUser(user);
        return galleryRepository.save(gallery);
    }

    public List<GalleryDTO> getUserGalleries(Long userId) {
        return galleryRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GalleryDTO> getAllGalleries() {
        return galleryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Gallery getGalleryById(Long id) {
        return galleryRepository.findById(id).orElse(null);
    }

    private GalleryDTO convertToDTO(Gallery gallery) {
        if (gallery == null) return null;
        return new GalleryDTO(
                gallery.getId(),
                gallery.getName(),
                gallery.getUser().getUsername(),
                gallery.getPhotos().size()
        );
    }
}