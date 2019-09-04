package tu.diplomna.guessGame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tu.diplomna.guessGame.Services.PostService;

@RestController
@RequestMapping("/api/post")
public class PostRestController {

    private PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/like/{id}")
    public String likePost(@PathVariable int id) {

        return postService.likePost(id);
    }


    @PostMapping("/answer/{id}")
    public String answerPost(@PathVariable int id, @RequestParam("answer") String answer) {
        return postService.answerPost(id, answer);
    }
}
