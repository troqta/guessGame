package tu.diplomna.guessGame.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PostBindingModel {

    @NotNull
    @Length(min = 5, max = 30, message = "Post title must be between 5 and 30 symbols long")
    private String title;

    @NotNull
    @Length(min = 5, max = 30, message = "Post answer must be between 5 and 30 symbols long")
    private String answer;

    private String description;

    public PostBindingModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
