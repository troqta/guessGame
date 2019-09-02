package tu.diplomna.guessGame.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserBindingModel {

    @NotNull
    @Length(min = 5, max = 25, message = "Username must be between 5 and 25 symbols long")
    private String username;

    @NotNull
    @Length(min = 5, max = 25, message = "Password must be between 5 and 25 symbols long")
    private String password;

    @NotNull
    @Email
    private String email;

    public UserBindingModel() {
    }

    public UserBindingModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserBindingModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
