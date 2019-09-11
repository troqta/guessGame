package tu.diplomna.guessGame.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

public class UserEditBindingModel {

    @Length(min = 5, max = 25, message = "Password must be between 5 and 25 symbols long")
    private String password;

    private String oldPassword;

    @Email
    private String email;

    public UserEditBindingModel() {
    }

    public UserEditBindingModel(String password, String oldPassword, String email) {
        this.password = password;
        this.oldPassword = oldPassword;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "UserEditBindingModel{" +
                "password='" + password + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
