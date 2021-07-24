package engine.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedQuizRepository extends JpaRepository<CompletedQuiz, Long> {
    Page<CompletedQuiz> findAllByUserName(String userName, Pageable paging);
}
