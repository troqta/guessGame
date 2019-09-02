package tu.diplomna.guessGame.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import tu.diplomna.guessGame.entities.Role;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.models.UserBindingModel;
import tu.diplomna.guessGame.repositories.RoleRepository;
import tu.diplomna.guessGame.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;

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
        if(model.getUsername().equals("") || model.getEmail().equals("") || model.getPassword().equals("")){
            return false;
        }
        Optional<UserDetails> optionalUser = userRepository.findByUsername(model.getUsername());

        if(optionalUser.isPresent()){
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
}
