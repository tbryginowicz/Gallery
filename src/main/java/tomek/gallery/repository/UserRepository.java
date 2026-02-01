package tomek.gallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomek.gallery.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
