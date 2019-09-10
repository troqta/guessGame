package tu.diplomna.guessGame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.Services.PostService;
import tu.diplomna.guessGame.Services.UserService;
import tu.diplomna.guessGame.models.UserBindingModel;
import tu.diplomna.guessGame.utils.Util;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping
    public String home(Model model) {
        if (!Util.isAnonymous()) {
            if(Util.currentUser() instanceof String){
                model.addAttribute("user", Util.currentUser());

            }
            else {
                model.addAttribute("user", ((UserDetails)Util.currentUser()).getUsername());

            }
        }

        model.addAttribute("mostLiked", postService.getAllPostsByLikes());
        model.addAttribute("mostAnswered", postService.getAllPostsByAnswers());

        model.addAttribute("view", "home");
        return "base-layout";
    }

    @GetMapping("/login")
    public String login(Model model){
        if(!Util.isAnonymous()){
            return "redirect:/";
        }

        model.addAttribute("view", "login");

        return "base-layout";
    }


    @GetMapping("/register")
    public String registerPage(Model model){
        if(!Util.isAnonymous()){
            return "redirect:/";
        }

        model.addAttribute("view", "register");

        return "base-layout";
    }



    @PostMapping("/register")
    public String register(@Valid UserBindingModel userBindingModel, BindingResult errors, Model model){

        if(!userService.registerUser(userBindingModel, errors) || errors.hasErrors()){

            model.addAttribute("view", "register");
            model.addAttribute("errors", errors.getAllErrors());

            return "base-layout";
        }
        return "redirect:/login";
    }

    @GetMapping("/test")
    public String testPage(Model model){

        model.addAttribute("view", "test");

        return "base-layout";
    }


    @PostMapping("/test")
    public String test( @RequestParam("file") MultipartFile file){

        postService.test(file);

        return "redirect:/test";
    }
}
