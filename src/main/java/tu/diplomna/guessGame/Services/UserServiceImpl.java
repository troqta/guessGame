package tu.diplomna.guessGame.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import tu.diplomna.guessGame.custom.Storage;
import tu.diplomna.guessGame.entities.Role;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.UserBindingModel;
import tu.diplomna.guessGame.models.UserEditBindingModel;
import tu.diplomna.guessGame.repositories.RoleRepository;
import tu.diplomna.guessGame.repositories.UserRepository;
import tu.diplomna.guessGame.utils.Util;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    private Storage storage;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder, Storage storage) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.storage = storage;

        initialRoleAndAdminSetup();
    }

    private void initialRoleAndAdminSetup() {
        if (roleRepository.findAll().size() < 2) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        Optional optionalUser = userRepository.findByUsername("admin");
        if (!optionalUser.isPresent()) {
            User user = new User("admin", encoder.encode("admin"), "nomail@mail.com");

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            user.getAuthorities().add(adminRole);

            System.err.println(adminRole.toString());
            System.err.println(user.toString());

            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserDetails> optional = userRepository.findByUsername(s);
        if (optional.isPresent()) {
            return optional.get();

        }
        throw new UsernameNotFoundException("User with username " + s + " not found!");

    }

    @Override
    public boolean registerUser(UserBindingModel model, BindingResult errors) {
        if (!Util.isAnonymous()) {
            return false;
        }
        if (model.getUsername().equals("") || model.getEmail().equals("") || model.getPassword().equals("")) {
            return false;
        }
        Optional<UserDetails> optionalUser = userRepository.findByUsername(model.getUsername());

        if (optionalUser.isPresent()) {
            errors.addError(new ObjectError("Username", "Username is already taken!"));
            return false;
        }
        User user = new User();
        user.setUsername(model.getUsername());
        user.setPassword(encoder.encode(model.getPassword()));
        user.setEmail(model.getEmail());

        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(UserEditBindingModel model, BindingResult errors, MultipartFile file) {
        if (Util.isAnonymous()) {
            return false;
        }
        User user = (User) Util.currentUser();
        boolean changed = false;
        if (!model.getPassword().isEmpty()) {
            if (!model.getOldPassword().isEmpty()) {
                if (!encoder.matches(model.getOldPassword(), user.getPassword())) {
                    errors.addError(new ObjectError("Password", "Old password doesnt match!"));
                    return false;
                }
                user.setPassword(encoder.encode(model.getPassword()));
                changed = true;
            } else {
                errors.addError(new ObjectError("Password", "Please input your old password to change the new one!"));
                return false;
            }
        }

        if (!file.isEmpty()) {
            String filePath = storage.storeWithCustomLocation(user.getUsername(), file, user.getProfilePicture().substring(user.getProfilePicture().lastIndexOf("/")));
            user.setProfilePicture(filePath);
            changed = true;
        }

        if (!model.getEmail().isEmpty()){
            user.setEmail(model.getEmail());
            changed = true;
        }

        if(changed){
            userRepository.save(user);
        }

        return changed;
    }
}
