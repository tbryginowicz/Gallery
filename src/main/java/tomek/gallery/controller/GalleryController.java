package tomek.gallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import tomek.gallery.dto.GalleryDTO;
import tomek.gallery.dto.PhotoDTO;
import tomek.gallery.entity.Gallery;
import tomek.gallery.entity.Photo;
import tomek.gallery.entity.User;
import tomek.gallery.service.GalleryService;
import tomek.gallery.service.PhotoService;
import tomek.gallery.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewGalleries(Model model, Authentication auth) {
        String username = auth.getName();
        User user = userService.findByUsername(username);

        List<GalleryDTO> galleries = galleryService.getUserGalleries(user.getId());
        model.addAttribute("galleries", galleries);
        model.addAttribute("username", username);

        return "gallery-list";
    }

    @GetMapping("/{id}")
    public String viewPhotos(@PathVariable Long id, Model model, Authentication auth) {
        Gallery gallery = galleryService.getGalleryById(id);

        if (gallery == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gallery not found");
        }
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        if (!gallery.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        List<PhotoDTO> photos = photoService.getGalleryPhotos(id);

        model.addAttribute("gallery", gallery);
        model.addAttribute("photos", photos);
        model.addAttribute("galleryId", id);

        return "photo-view";
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id, Authentication auth) {
        Photo photo = photoService.getPhotoById(id);

        if (photo == null || photo.getPhotoData() == null) {
            return ResponseEntity.notFound().build();
        }
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        if (!photo.getGallery().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo.getPhotoData());
    }
}