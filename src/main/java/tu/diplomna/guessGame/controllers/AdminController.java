package tu.diplomna.guessGame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tu.diplomna.guessGame.Services.PostService;
import tu.diplomna.guessGame.Services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    private PostService postService;

    @Autowired
    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/users")
    public String usersPage(Model model) {

        model.addAttribute("view", "admin/users");

        return "base-layout";
    }
}
