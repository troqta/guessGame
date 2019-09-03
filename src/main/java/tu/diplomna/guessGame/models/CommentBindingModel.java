package tu.diplomna.guessGame.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CommentBindingModel {

    @NotNull
    @Length(min = 10, max = 50, message = "Comment must be between 10 and 50 symbols long.")
    private String content;

    public CommentBindingModel(String content) {
        this.content = content;
    }

    public CommentBindingModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
