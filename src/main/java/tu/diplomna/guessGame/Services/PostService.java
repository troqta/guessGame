package tu.diplomna.guessGame.Services;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.entities.Post;
import tu.diplomna.guessGame.models.CommentBindingModel;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.models.PostUpdateBindingModel;

public interface PostService {

    boolean createPost(PostBindingModel model, BindingResult errors, MultipartFile file);

    boolean updatePost(PostUpdateBindingModel model, int id, BindingResult errors);

    boolean likePost(int id);

    boolean deletePost(int id);

    boolean answerPost(int id);

    boolean addComment(int id, CommentBindingModel model, BindingResult errors);

    Post getPost(int id);
}
