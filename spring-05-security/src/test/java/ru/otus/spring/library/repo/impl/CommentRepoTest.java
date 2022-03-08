package ru.otus.spring.library.repo.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import ru.otus.spring.library.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с комментами должно")
@DataJpaTest
class CommentRepoTest {

    private static final int COMMENTS_BEFORE_SIZE = 4;
    private static final LocalDateTime COMMENT_CORRECT_DATE = LocalDateTime.parse("2015-12-18T17:53:02.594");
    private static final String COMMENT_CORRECT_BODY = "Очень крутая книга";
    private static final Long COMMENT_CORRECT_ID = 1L;

    private static final Long COMMENT_NEW_ID = 5L;
    private static final String COMMENT_NEW_BODY = "Книжек еще не написал";

    @Autowired
    private JpaRepository<Comment, Long> commentsRepository;

    @Autowired
    private TestEntityManager em;

    @BeforeTransaction
    void beforeTransaction() {
        System.out.println("-----------");
    }

    @AfterTransaction
    void afterTransaction() {
        System.out.println("-----------");
    }

    @Test
    @DisplayName("возвращать ожидаемый коммент по id")
    void getById() {
        Comment commentExpected = Comment.builder()
                .body(COMMENT_CORRECT_BODY)
                .id(COMMENT_CORRECT_ID)
                .date(COMMENT_CORRECT_DATE)
                .build();

        assertThat(commentsRepository.findById(commentExpected.getId()))
                .isNotEmpty()
                .get()
                .isEqualTo(commentExpected);
    }

    @Test
    @DisplayName("обновлять тело комментария")
    void updateById() {

        Comment comment = Comment.builder()
                .id(COMMENT_CORRECT_ID)
                .body(COMMENT_CORRECT_BODY)
                .build();

        commentsRepository.save(comment);
        Comment authorAfterUpdate = em.find(Comment.class, COMMENT_CORRECT_ID);

        assertThat(authorAfterUpdate)
                .hasFieldOrPropertyWithValue("body", COMMENT_CORRECT_BODY)
                .hasFieldOrPropertyWithValue("id", COMMENT_CORRECT_ID);
    }

    @Test
    @DisplayName("обновлять тело комментария")
    void save() {
        Comment commentExpected = Comment.builder()
                .id(COMMENT_NEW_ID)
                .body(COMMENT_NEW_BODY)
                .build();


        assertThat(commentsRepository.save(new Comment(COMMENT_NEW_BODY)))
                .hasFieldOrPropertyWithValue("body", commentExpected.getBody())
                .hasFieldOrPropertyWithValue("id", commentExpected.getId())
                .hasNoNullFieldsOrPropertiesExcept("date");
    }

    @Test
    @DisplayName("получать список комментариев")
    void getAll() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        List<Comment> comments = commentsRepository.findAll();
        assertThat(comments).isNotNull().hasSize(COMMENTS_BEFORE_SIZE)
                .allMatch(c -> !c.getBody().equals(""))
                .allMatch(c -> c.getDate() != null);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("удалять комментарий")
    void deleteById() {

        commentsRepository.deleteById(COMMENT_CORRECT_ID);

        assertThat(commentsRepository.findAll())
                .hasSize(COMMENTS_BEFORE_SIZE - 1);

        assertThat(commentsRepository.findById(COMMENT_CORRECT_ID))
                .isEmpty();
    }
}