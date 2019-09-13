package tu.diplomna.guessGame.Services;

import org.slf4j.Logger;
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

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    private Storage storage;

    private Logger logger;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder, Storage storage, Logger logger) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.storage = storage;
        this.logger = logger;

        initialRoleAndAdminSetup();
    }

    private void initialRoleAndAdminSetup() {
        if (roleRepository.findAll().size() < 3) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_OWNER"));
            logger.info("Saved roles to DB");
        }

        Optional optionalUser = userRepository.findByUsername("admin");
        if (!optionalUser.isPresent()) {
            User user = new User("admin", encoder.encode("admin"), "nomail@mail.com");

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Role userRole = roleRepository.findByName("ROLE_USER");

            user.getAuthorities().add(adminRole);
            user.getAuthorities().add(userRole);


            userRepository.save(user);

            logger.info("Saved initial admin user to DB", user);
        }

        optionalUser = userRepository.findByUsername("owner");
        if (!optionalUser.isPresent()) {
            User user = new User("owner", encoder.encode("owner"), "nomail@mail.com");

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Role ownerRole = roleRepository.findByName("ROLE_OWNER");
            Role userRole = roleRepository.findByName("ROLE_USER");

            user.getAuthorities().add(adminRole);
            user.getAuthorities().add(ownerRole);
            user.getAuthorities().add(userRole);

            userRepository.save(user);

            logger.info("Saved initial owner user to DB", user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserDetails> optional = userRepository.findByUsername(s);
        if (optional.isPresent()) {
            return optional.get();

        }
        logger.error("User with username " + s + " not found!");
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
        Role role = roleRepository.findByName("ROLE_USER");
        user.getAuthorities().add(role);
        user.setUsername(model.getUsername());
        user.setPassword(encoder.encode(model.getPassword()));
        user.setEmail(model.getEmail());

        userRepository.save(user);
        logger.info("Registered user ", user);
        return true;
    }

    @Override
    public boolean updateUser(UserEditBindingModel model, BindingResult errors, MultipartFile file) {
        if (Util.isAnonymous()) {
            return false;
        }
        User user = (User) Util.currentUser();
        boolean changed = false;
        if (model.getPassword() != null &&!model.getPassword().isEmpty()) {
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

        if (file != null && !file.isEmpty()) {
            String filePath = storage.storeWithCustomLocation(user.getUsername(), file, user.getProfilePicture().substring(user.getProfilePicture().lastIndexOf("/")));
            user.setProfilePicture(filePath);
            changed = true;
        }

        if (model.getEmail() != null && !model.getEmail().isEmpty()){
            user.setEmail(model.getEmail());
            changed = true;
        }

        if(changed){
            userRepository.save(user);
        }
        return changed;
    }

    @Override
    public boolean banUser(int id) {
        if(Util.isAnonymous()){
            return false;
        }
        User currentUser = (User) Util.currentUser();
        if(!currentUser.isAdmin()){
            return false;
        }

        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            return false;
        }
        User user = optionalUser.get();

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if(user.getAuthorities().contains(adminRole) && !currentUser.isOwner()){
            return false;
        }


        user.setEnabled(false);

        userRepository.save(user);

        logger.info("Admin " + currentUser.getUsername() + " banned user " + user.getUsername());

        return true;
    }

    @Override
    public boolean unbanUser(int id) {
        if(Util.isAnonymous()){
            return false;
        }
        User currentUser = (User) Util.currentUser();
        if(!currentUser.isAdmin()){
            return false;
        }

        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            return false;
        }
        User user = optionalUser.get();

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if(user.getAuthorities().contains(adminRole) && !currentUser.isOwner()){
            return false;
        }

        user.setEnabled(true);

        userRepository.save(user);

        logger.info("Admin " + currentUser.getUsername() + " unbanned user " + user.getUsername());


        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkAvailability(String username) {

        Optional<UserDetails> user = userRepository.findByUsername(username);

        return !user.isPresent();
    }

    @Override
    public boolean makeAdmin(int id) {
        if(Util.isAnonymous()){
            return false;
        }

        User currentUser;
        if (Util.currentUser() instanceof String) {
            currentUser = (User) userRepository.findByUsername((String) Util.currentUser()).get();

        } else {
            currentUser = (User) Util.currentUser();

        }
        if(!currentUser.isOwner()){
            return false;
        }
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            return false;
        }

        User user = optionalUser.get();

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if(user.getAuthorities().contains(adminRole)){
            return false;
        }
        user.getAuthorities().add(adminRole);

        userRepository.save(user);

        return true;
    }

    @Override
    public boolean removeAdmin(int id) {
        if(Util.isAnonymous()){
            return false;
        }

        User currentUser;
        if (Util.currentUser() instanceof String) {
            currentUser = (User) userRepository.findByUsername((String) Util.currentUser()).get();

        } else {
            currentUser = (User) Util.currentUser();

        }
        if(!currentUser.isOwner()){
            return false;
        }
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            return false;
        }

        User user = optionalUser.get();

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if(!user.getAuthorities().contains(adminRole)){
            return false;
        }
        user.getAuthorities().remove(adminRole);

        userRepository.save(user);

        return true;
    }

    @Override
    public boolean giveOwner(int id) {
        if(Util.isAnonymous()){
            return false;
        }

        User currentUser;
        if (Util.currentUser() instanceof String) {
            currentUser = (User) userRepository.findByUsername((String) Util.currentUser()).get();

        } else {
            currentUser = (User) Util.currentUser();

        }
        if(!currentUser.isOwner()){
            return false;
        }
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            return false;
        }

        User user = optionalUser.get();

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role ownerRole = roleRepository.findByName("ROLE_OWNER");
        Role userRole = roleRepository.findByName("ROLE_USER");

        if(user.getAuthorities().contains(ownerRole)){
            return false;
        }
        user.getAuthorities().add(adminRole);
        user.getAuthorities().add(ownerRole);

        userRepository.save(user);

        currentUser = userRepository.findById(currentUser.getId()).get();


        currentUser.getAuthorities().remove(ownerRole);

        userRepository.save(currentUser);

        return true;
    }
}
