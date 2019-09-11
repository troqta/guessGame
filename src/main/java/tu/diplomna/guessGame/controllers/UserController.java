package tu.diplomna.guessGame.controllers;

import com.sun.net.httpserver.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.Services.UserService;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.UserEditBindingModel;
import tu.diplomna.guessGame.repositories.UserRepository;
import tu.diplomna.guessGame.utils.Util;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/edit")
    public String editUserPage(Model model) {
        if (Util.isAnonymous()) {
            return "redirect:/error/403";
        }

        User user;

        if (Util.currentUser() instanceof String) {
            user = (User) userService.loadUserByUsername((String) Util.currentUser());

        } else {
            user = (User) Util.currentUser();

        }


        model.addAttribute("user", user);
        model.addAttribute("view", "user/edit");

        return "base-layout";
    }

    @GetMapping("/profile")
    public String profilePage(Model model){
        if (Util.isAnonymous()) {
            return "redirect:/error/403";
        }

        User user;

        if (Util.currentUser() instanceof String) {
            user = (User) userService.loadUserByUsername((String) Util.currentUser());

        } else {
            user = (User) Util.currentUser();

        }

        model.addAttribute("user", user);

        model.addAttribute("view", "/user/profile");

        return "base-layout";
    }

    @PostMapping("/edit")
    public String editUser(@Valid UserEditBindingModel userEditBindingModel, BindingResult errors, @RequestParam(value = "file", required = false) MultipartFile file, Model model, HttpServletResponse response) {
        if(Util.isAnonymous()){
            return "redirect:/error/403";
        }

        if(!userService.updateUser(userEditBindingModel, errors, file) || errors.hasErrors()){
            model.addAttribute("errors", errors.getAllErrors());
            response.addHeader("errors", errors.getAllErrors().toString());
            User user;

            if (Util.currentUser() instanceof String) {
                user = (User) userService.loadUserByUsername((String) Util.currentUser());

            } else {
                user = (User) Util.currentUser();

            }


            model.addAttribute("user", user);
            model.addAttribute("view", "user/edit");

            return "base-layout";
        }

        return "redirect:/user/profile";
    }
}
