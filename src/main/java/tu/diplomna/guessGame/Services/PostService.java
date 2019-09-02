package tu.diplomna.guessGame.Services;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.entities.Post;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.models.PostUpdateBindingModel;

public interface PostService {

    boolean createPost(PostBindingModel model, BindingResult errors, MultipartFile file);

    void updatePost(PostUpdateBindingModel model, int id);

    void likePost(int id);

    void deletePost(int id);

    void answerPost(int id);

    void addComment(int id);

    Post getPost(int id);
}
