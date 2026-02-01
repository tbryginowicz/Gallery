package tomek.gallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tomek.gallery.dto.GalleryDTO;
import tomek.gallery.dto.UserDTO;
import tomek.gallery.entity.Gallery;
import tomek.gallery.entity.User;
import tomek.gallery.service.GalleryService;
import tomek.gallery.service.PhotoService;
import tomek.gallery.service.UserService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public String adminPanel(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        List<GalleryDTO> allGalleries = galleryService.getAllGalleries();

        model.addAttribute("users", users);
        model.addAttribute("allGalleries", allGalleries);

        return "admin-panel";
    }

    @PostMapping("/user/create")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam(defaultValue = "false") boolean isAdmin) {
        userService.createUser(username, password, isAdmin);
        return "redirect:/admin";
    }

    @PostMapping("/gallery/create")
    public String createGallery(@RequestParam String name,
                                @RequestParam Long userId) {
        User user = userService.findByUsername(
                userService.getAllUsers().stream()
                        .filter(u -> u.getId().equals(userId))
                        .findFirst()
                        .map(UserDTO::getUsername)
                        .orElse(null)
        );

        if (user != null) {
            galleryService.createGallery(name, user);
        }

        return "redirect:/admin";
    }

    @PostMapping("/photo/upload")
    public String uploadPhotos(@RequestParam("files") MultipartFile[] files,
                               @RequestParam Long galleryId) throws IOException {
        Gallery gallery = galleryService.getGalleryById(galleryId);

        if (gallery != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    photoService.savePhoto(file, gallery);
                }
            }
        }

        return "redirect:/admin";
    }
}