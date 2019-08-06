package tu.diplomna.guessGame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tu.diplomna.guessGame.utils.Util;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UsersConnectionRepository usersConnectionRepository;

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
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }
}
