package engine.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "completed_quiz")
public class CompletedQuiz {

    @JsonIgnore
    private long completedQuizId;

    private long id;
    private LocalDateTime completedAt;

    @JsonIgnore
    private String userName;

    public CompletedQuiz() {}

    public CompletedQuiz(long id, LocalDateTime completedAt, String userName) {
        this.id = id;
        this.completedAt = completedAt;
        this.userName = userName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getCompletedQuizId() {
        return completedQuizId;
    }

    public void setCompletedQuizId(long completedQuizId) {
        this.completedQuizId = completedQuizId;
    }

    @Column(name = "quiz_id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "time", nullable = false)
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Column(name = "username", nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
