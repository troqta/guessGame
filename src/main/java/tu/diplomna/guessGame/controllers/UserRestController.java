package tu.diplomna.guessGame.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.Services.UserService;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.RestResponseModel;
import tu.diplomna.guessGame.models.UserEditBindingModel;
import tu.diplomna.guessGame.utils.Util;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    UserService userService;


    @PostMapping("/edit")
    public RestResponseModel editUser(@Valid UserEditBindingModel userEditBindingModel, BindingResult errors, @RequestParam(value = "file", required = false) MultipartFile file, Model model) {
        if (Util.isAnonymous()) {
            return new RestResponseModel(403, "You must be logged in");
        }
        List<String> errorsList = new ArrayList<>();

        for (ObjectError er :
                errors.getAllErrors()) {
            errorsList.add(er.getDefaultMessage());
        }

        if (!userService.updateUser(userEditBindingModel, errors, file) || errors.hasErrors()) {
            return new RestResponseModel(400, errorsList);
        }

        return new RestResponseModel(200, "OK");
    }

}
