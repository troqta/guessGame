package tu.diplomna.guessGame.models;

public class PostUpdateBindingModel {

    private String description;

    private String answer;

    public PostUpdateBindingModel() {
    }

    public PostUpdateBindingModel(String description, String answer) {
        this.description = description;
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
