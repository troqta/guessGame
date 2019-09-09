package tu.diplomna.guessGame.entities;

import tu.diplomna.guessGame.utils.Util;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String answer;

    private String description;

    private String picture;

    @ManyToOne
    private User author;

    private Timestamp createdOn;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
            })
    @JoinTable(name = "liked_posts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
            })
    @JoinTable(name = "answered_posts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> answers;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;


    public Post(){
        likes = new HashSet<>();
        answers = new HashSet<>();
        comments = new ArrayList<>();
        createdOn = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public Set<User> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<User> answers) {
        this.answers = answers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

//    public boolean isAdminOrAuthor(){
//
//        if(Util.isAnonymous()){
//            return false;
//        }
//        User user = (User) Util.currentUser();
//
//
//        return user.isAdmin() || this.author.equals(user);
//    }

    public boolean hasAnswered(){
        if(Util.isAnonymous()){
            return false;
        }

        User user = (User) Util.currentUser();


        return this.answers.contains(user);
    }

    public boolean hasLiked(){
        if(Util.isAnonymous()){
            return false;
        }

        User user = (User) Util.currentUser();


        return this.likes.contains(user);
    }

    public int getAnswersCount(){
        return this.answers.size();
    }

    public int getLikesCount(){
        return this.likes.size();
    }
}
