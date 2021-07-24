package engine.quiz;

import engine.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class QuizRestController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompletedQuizRepository completedQuizRepository;

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable Long id)
            throws ResponseStatusException {
        return this.quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The id of quiz does not exist."));
    }

    @GetMapping("/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        Integer pageSize = 10;
        String sortBy = "id";

        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return this.quizRepository.findAll(paging);
    }

    @GetMapping("/api/quizzes/completed")
    public Page<CompletedQuiz> getCompletedQuizzes(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestParam(defaultValue = "0") Integer page) {
        Integer pageSize = 10;
        String sortBy = "completedAt";

        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        return this.completedQuizRepository.findAllByUserName(userDetails.getUsername(), paging);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public QuizFeedBack solveQuiz(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long id, @RequestBody Answer answer)
            throws ResponseStatusException {
        Quiz quiz = this.quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The id of quiz does not exist."));

        if (Arrays.equals(answer.getAnswer(), quiz.getAnswer())) {
            System.out.println(userDetails.getUsername() + " " + id);
            this.completedQuizRepository.save(new CompletedQuiz(id, LocalDateTime.now(), userDetails.getUsername()));
            return new QuizFeedBack(true, "Congratulations, you're right!");
        } else {
            return new QuizFeedBack(false, "Wrong answer! Please, try again.");
        }
    }

    @PostMapping("/api/quizzes")
    public Quiz addQuiz(@AuthenticationPrincipal UserDetails userDetails,
                        @Valid @RequestBody Quiz quiz) {
        quiz.setOwner(userDetails.getUsername());
        return this.quizRepository.save(quiz);
    }

    @RequestMapping("/api/quizzes/{id}")
    public void deleteQuiz(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable Long id)
            throws ResponseStatusException {
        Quiz quiz = this.quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The id of quiz does not exist."));

        if (quiz.getOwner().equals(userDetails.getUsername())) {
            this.quizRepository.deleteById(id);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The user is not the author of this quiz.");
        }
    }


}
