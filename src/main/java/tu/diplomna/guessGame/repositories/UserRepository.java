package tu.diplomna.guessGame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import tu.diplomna.guessGame.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<UserDetails> findByUsername(String s);

    Optional<User> findByProfilePicture(String profilePicture);
}
