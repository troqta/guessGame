package tu.diplomna.guessGame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(connection.getDisplayName());
        user.setProfilePicture(connection.getImageUrl());
//        Facebook facebook = (Facebook) connection.getApi();
//        String[] fields = { "id", "about", "age_range", "birthday", "cover", "currency", "devices", "education", "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type", "is_verified", "languages", "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political", "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings", "significant_other", "sports", "test_group", "timezone", "third_party_id", "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift", "website", "work"};
//        org.springframework.social.facebook.api.User fbuser = ((Facebook) connection.getApi()).fetchObject("me", org.springframework.social.facebook.api.User.class, fields);

//        System.out.println(connection.getImageUrl());
//        facebook.friendOperations().getFriendLists();
//        System.err.println(facebook.friendOperations().getFriendLists());
        user.setPassword(encoder.encode(UUID.randomUUID().toString()));
        Optional<User> repoUser = userRepository.findByProfilePicture(connection.getImageUrl());
        if(repoUser.isPresent()){
            return user.getUsername();
        }
        userRepository.save(user);

        return user.getUsername();
    }
}
