package tomek.gallery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tomek.gallery.service.UserService;
import tomek.gallery.repository.UserRepository;

@Component
public class StartupRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("Application started! Creating a demo admin account...");

        if (userRepository.findByUsername("admin").isEmpty()) {
            userService.createUser("admin", "admin", true);
            System.out.println("Created ADMIN:\n login: admin\n passwrd: admin");
        } else {
            System.out.println("Admin user already exists");
        }
    }
}