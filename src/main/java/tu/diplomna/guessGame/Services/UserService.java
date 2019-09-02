package tu.diplomna.guessGame.Services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.UserBindingModel;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    boolean registerUser(UserBindingModel model, BindingResult errors);
}
