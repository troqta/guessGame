package tu.diplomna.guessGame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tu.diplomna.guessGame.entities.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(int id);
}
