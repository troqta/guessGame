package tu.diplomna.guessGame.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.custom.Storage;
import tu.diplomna.guessGame.entities.Post;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.models.PostUpdateBindingModel;
import tu.diplomna.guessGame.repositories.PostRepository;
import tu.diplomna.guessGame.utils.Util;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private Storage storage;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, Storage storage) {
        this.postRepository = postRepository;
        this.storage = storage;
    }

    @Override
    public boolean createPost(PostBindingModel model, BindingResult errors, MultipartFile file) {
        if (file.isEmpty()) {
            errors.addError(new ObjectError("File", "Please upload a file"));
            return false;
        }

        User author = (User) Util.currentUser();
        Post post = new Post();
        post.setAnswer(model.getAnswer());
        post.setDescription(model.getDescription());
        post.setTitle(model.getTitle());
        post.setAuthor(author);
        String path = storage.storeWithCustomLocation(post.getTitle(), file);
        post.setPicture(path);
        //TODO add to author posts
        postRepository.save(post);

        return true;
    }

    @Override
    public void updatePost(PostUpdateBindingModel model, int id) {

    }

    @Override
    public void likePost(int id) {

    }

    @Override
    public void deletePost(int id) {

    }

    @Override
    public void answerPost(int id) {

    }

    @Override
    public void addComment(int id) {

    }

    @Override
    public Post getPost(int id) {
        Optional<Post> post = postRepository.findById(id);

        return post.orElse(null);

    }
}
