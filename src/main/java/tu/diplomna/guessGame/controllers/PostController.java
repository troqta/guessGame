package tu.diplomna.guessGame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.Services.PostService;
import tu.diplomna.guessGame.entities.Post;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.utils.Util;

import javax.validation.Valid;

@Controller
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public String postDetails(@PathVariable int id, Model model) {
        Post post = postService.getPost(id);

        if (post == null) {
            return "redirect:/error/404";
        }

        model.addAttribute("post", post);
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

}
