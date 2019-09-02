package tu.diplomna.guessGame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tu.diplomna.guessGame.entities.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findById(int id);
}
