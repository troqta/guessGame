package tu.diplomna.guessGame.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

public class Util {
    public static Object currentUser() {
        if (Util.isAnonymous()) {
            return null;
        }
        if (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal() instanceof Principal) {
            return (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
        }
        else return SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public static boolean isAnonymous() {
        return AnonymousAuthenticationToken.class ==
                SecurityContextHolder
                        .getContext()
                        .getAuthentication().getClass();
    }
}
