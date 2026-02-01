package tomek.gallery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tomek.gallery.dto.PhotoDTO;
import tomek.gallery.entity.Gallery;
import tomek.gallery.entity.Photo;
import tomek.gallery.repository.PhotoRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo savePhoto(MultipartFile file, Gallery gallery) throws IOException {
        Photo photo = new Photo();
        photo.setFileName(file.getOriginalFilename());
        photo.setPhotoData(file.getBytes());
        photo.setGallery(gallery);
        return photoRepository.save(photo);
    }

    public List<PhotoDTO> getGalleryPhotos(Long galleryId) {
        return photoRepository.findByGalleryId(galleryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }

    private PhotoDTO convertToDTO(Photo photo) {
        if (photo == null) return null;
        return new PhotoDTO(
                photo.getId(),
                photo.getFileName(),
                photo.getGallery().getId()
        );
    }
}