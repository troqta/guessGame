package tu.diplomna.guessGame.Services;

import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.custom.Storage;
import tu.diplomna.guessGame.entities.Comment;
import tu.diplomna.guessGame.entities.Post;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.CommentBindingModel;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.models.PostUpdateBindingModel;
import tu.diplomna.guessGame.repositories.CommentRepository;
import tu.diplomna.guessGame.repositories.PostRepository;
import tu.diplomna.guessGame.repositories.UserRepository;
import tu.diplomna.guessGame.utils.Util;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private Storage storage;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, Storage storage, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.storage = storage;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean createPost(PostBindingModel model, BindingResult errors, MultipartFile file) {
        if(Util.isAnonymous()){
            return false;
        }
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
        author.getPosts().add(post);
        postRepository.save(post);
        userRepository.save(author);

        return true;
    }

    @Override
    public boolean updatePost(PostUpdateBindingModel model, int id, BindingResult errors) {
        if(Util.isAnonymous()){
            return false;
        }
        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            errors.addError(new ObjectError("Post", "Post doesn't exist"));
            return false;
        }
        Post post = optionalPost.get();
        boolean changed = false;
        if(!model.getDescription().equals("")){
            post.setDescription(model.getDescription());
            changed = true;
        }

        if(!model.getAnswer().equals("")){
            post.setAnswer(model.getAnswer());
            changed = true;
        }

        if(changed){
            postRepository.save(post);
        }

        return changed;
    }

    @Override
    public boolean likePost(int id) {
        if(Util.isAnonymous()){
            return false;
        }
        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            return false;
        }
        Post post = optionalPost.get();
        User user = (User) Util.currentUser();
        if(post.getLikes().contains(user) || user.getLikedPosts().contains(post)){
            return false;
        }
        post.getLikes().add(user);
        user.getLikedPosts().add(post);

        postRepository.save(post);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean deletePost(int id) {
        if(Util.isAnonymous()){
            return false;
        }
        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            return false;
        }
        Post post = optionalPost.get();
        User user = (User) Util.currentUser();
        if(!post.isAdminOrAuthor()){
            return false;
        }

        user.getLikedPosts().remove(post);
        user.getPosts().remove(post);
        user.getAnsweredPosts().remove(post);

        postRepository.delete(post);
        userRepository.save(user);

        return true;

    }

    @Override
    public boolean answerPost(int id, String answer) {
        if(Util.isAnonymous()){
            return false;
        }
        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            return false;
        }
        Post post = optionalPost.get();
        User user = (User) Util.currentUser();
        if(post.getAnswers().contains(user) || user.getAnsweredPosts().contains(post)){
            return false;
        }


        if(!answer.equals(post.getAnswer())){
            return false;
        }
        post.getAnswers().add(user);
        user.getAnsweredPosts().add(post);

        postRepository.save(post);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean addComment(int id, CommentBindingModel model, BindingResult errors) {
        if(Util.isAnonymous()){
            return false;
        }
        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            return false;
        }
        Comment comment = new Comment();
        User user = (User) Util.currentUser();
        comment.setAuthor(user);
        comment.setContent(model.getContent());

        Post post = optionalPost.get();

        post.getComments().add(comment);
        commentRepository.save(comment);
        postRepository.save(post);

        return true;
    }

    @Override
    public Post getPost(int id) {
        Optional<Post> post = postRepository.findById(id);

        return post.orElse(null);

    }
}
