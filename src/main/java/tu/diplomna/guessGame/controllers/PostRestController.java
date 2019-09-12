package tu.diplomna.guessGame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.Services.PostService;
import tu.diplomna.guessGame.models.PostBindingModel;
import tu.diplomna.guessGame.models.RestResponseModel;
import tu.diplomna.guessGame.utils.Util;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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


    @PostMapping("/create")
    public RestResponseModel createPost(@Valid PostBindingModel postBindingModel, BindingResult errors, @RequestParam("file") MultipartFile file, Model model) {
        if (Util.isAnonymous()) {
            return new RestResponseModel(403, "Access denied!");
        }
        if(!postService.createPost(postBindingModel, errors, file) || errors.hasErrors()){
            List<String> errorsList = new ArrayList<>();

            for (ObjectError er :
                    errors.getAllErrors()) {
                errorsList.add(er.getDefaultMessage());
            }
            return new RestResponseModel(400, errorsList);
        }
        return new RestResponseModel(200, "ОК");
    }
}
