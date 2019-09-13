package tu.diplomna.guessGame.Services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.UserBindingModel;
import tu.diplomna.guessGame.models.UserEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    boolean registerUser(UserBindingModel model, BindingResult errors);

    boolean updateUser(UserEditBindingModel model, BindingResult errors, MultipartFile file);

    boolean banUser(int id);
    boolean unbanUser(int id);

    List<User> getAllUsers();

    boolean checkAvailability(String username);

    boolean makeAdmin(int id);

    boolean removeAdmin(int id);

    boolean giveOwner(int id);
}
