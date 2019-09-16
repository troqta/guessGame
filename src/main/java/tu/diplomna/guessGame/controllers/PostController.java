package tu.diplomna.guessGame.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.Services.PostService;
import tu.diplomna.guessGame.Services.UserService;
import tu.diplomna.guessGame.entities.Post;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.CommentBindingModel;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.utils.Util;

import javax.validation.Valid;

@Controller
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    private UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String postDetails(@PathVariable int id, Model model) {
        Post post = postService.getPost(id);

        if (post == null) {
            return "redirect:/error/404";
        }
        User user;

        if (Util.currentUser() instanceof String) {
            user = (User) userService.loadUserByUsername((String) Util.currentUser());

        } else {
            user = (User) Util.currentUser();

        }

        model.addAttribute("post", post);
        model.addAttribute("user", user);
        model.addAttribute("view", "post/details");


        return "base-layout";
    }

    @GetMapping("/create")
    public String createPostPage(Model model) {
        if (Util.isAnonymous()) {
            return "redirect:/error/403";
        }
        model.addAttribute("view", "post/create");
        return "base-layout";
    }

    @PostMapping("/create")
    public String createPost(@Valid PostBindingModel postBindingModel, BindingResult errors, @RequestParam("file") MultipartFile file, Model model) {
        if (Util.isAnonymous()) {
            return "redirect:/error/403";
        }
        if(!postService.createPost(postBindingModel, errors, file) || errors.hasErrors()){
            model.addAttribute("errors", errors.getAllErrors());
            model.addAttribute("view", "post/create");

            return "base-layout";
        }


        return "redirect:/";
    }

    @GetMapping("edit/{id}")
    public String editPostPage(@PathVariable int id, Model model){
        if(Util.isAnonymous()){
            return "redirect:/error/403";
        }

        Post post = postService.getPost(id);

        if(post == null){
            return "redirect:/error/404";
        }
        if(!postService.isAdminOrAuthor(post)){
            return "redirect:/error/403";
        }

        model.addAttribute("post", post);
        model.addAttribute("view", "post/edit");

        return "base-layout";
    }

    @PostMapping("/comment/{id}")
    public String comment(@Valid CommentBindingModel commentBindingModel, BindingResult errors, @PathVariable int id, Model model){
        if(!postService.addComment(1, commentBindingModel, errors) || errors.hasErrors()){
            model.addAttribute("errors", errors.getAllErrors());
            model.addAttribute("view", "post/create");

            return "base-layout";
        }
        return "redirect:/post/" + id;
    }
}
