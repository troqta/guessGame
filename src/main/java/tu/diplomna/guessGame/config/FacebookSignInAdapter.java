package tu.diplomna.guessGame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import tu.diplomna.guessGame.entities.User;
import tu.diplomna.guessGame.repositories.UserRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
public class FacebookSignInAdapter implements SignInAdapter {

    private UserRepository userRepository;

    public FacebookSignInAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String signIn(
            String localUserId,
            Connection<?> connection,
            NativeWebRequest request) {
        Optional<User> optionalUser = userRepository.findByProfilePicture(connection.getImageUrl());
        if (!optionalUser.isPresent()) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            connection.getDisplayName(), null,
                            Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER"))));
        } else {
            User user = optionalUser.get();
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            user, null,
                            user.getAuthorities()));
        }
        return null;
    }
}
