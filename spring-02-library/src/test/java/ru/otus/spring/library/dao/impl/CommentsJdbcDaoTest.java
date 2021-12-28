package ru.otus.spring.library.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import ru.otus.spring.library.aop.AopDaoService;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с комментами должно")
@DataJpaTest
@Import({CommentsJdbcDao.class, AopDaoService.class})
class CommentsJdbcDaoTest {

    private static final int COMMENTS_BEFORE_SIZE = 4;
    private static final String COMMENT_CORRECT_FOR_REMOVE_BODY = "Для удаления";
    private static final Long COMMENT_CORRECT_FOR_REMOVE_ID = 4L;
    private static final LocalDateTime COMMENT_CORRECT_DATE = LocalDateTime.parse("2015-12-18T17:53:02.594");
    private static final String COMMENT_CORRECT_BODY = "Очень крутая книга";
    private static final Long COMMENT_CORRECT_ID = 1L;
    private static final String COMMENT_NEW_CORRECT_BODY = "Толкин-Джон Рональд Руэл";

    private static final Long COMMENT_NEW_ID = 5L;
    private static final String COMMENT_NEW_BODY = "Книжек еще не написал";

    private static final Long BOOK_CORRECT_ID = 2L;

    @Autowired
    private CommentsJdbcDao commentsJdbcDao;

    @Autowired
    private AopDaoService aopDaoService;

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
                .book(Book.builder().id(COMMENT_CORRECT_ID).build())
                .build();

        assertThat(commentsJdbcDao.getById(commentExpected.getId()))
                .isNotEmpty()
                .get()
                .isEqualTo(commentExpected);
    }

    @Test
    void getByBookId() {
        assertThat(commentsJdbcDao.getByBookId(BOOK_CORRECT_ID))
                .hasSize(1)
                .extracting("body", "id")
                .contains(tuple(COMMENT_CORRECT_FOR_REMOVE_BODY, COMMENT_CORRECT_FOR_REMOVE_ID));
    }

    @Test
    void updateById() {
        commentsJdbcDao.updateById(COMMENT_CORRECT_ID, new Comment(COMMENT_CORRECT_BODY));
        Comment authorAfterUpdate = em.find(Comment.class, COMMENT_CORRECT_ID);

        assertThat(authorAfterUpdate)
                .hasFieldOrPropertyWithValue("body", COMMENT_CORRECT_BODY)
                .hasFieldOrPropertyWithValue("id", COMMENT_CORRECT_ID);
    }

    @Test
    void save() {
        Comment commentExpected = Comment.builder()
                .id(COMMENT_NEW_ID)
                .book(em.find(Book.class, BOOK_CORRECT_ID))
                .body(COMMENT_NEW_BODY)
                .build();


        assertThat(commentsJdbcDao.save(new Comment(COMMENT_NEW_BODY, commentExpected.getBook())))
                .hasFieldOrPropertyWithValue("body", COMMENT_NEW_BODY)
                .hasFieldOrPropertyWithValue("id", COMMENT_NEW_ID)
                .hasFieldOrPropertyWithValue("book", em.find(Book.class, BOOK_CORRECT_ID))
                .hasNoNullFieldsOrPropertiesExcept("date");
    }

    @Test
    void getAll() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);


        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        List<Comment> comments = commentsJdbcDao.getAll();
        assertThat(comments).isNotNull().hasSize(COMMENTS_BEFORE_SIZE)
                .allMatch(c -> !c.getBody().equals(""))
                .allMatch(c -> c.getDate() != null)
                .allMatch(c -> c.getDate() != null)
                .allMatch(c -> c.getBook() != null);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }

    @Test
    void deleteById() {

        commentsJdbcDao.deleteById(COMMENT_CORRECT_ID);

        assertThat(commentsJdbcDao.getAll())
                .hasSize(COMMENTS_BEFORE_SIZE - 1);

        assertThatExceptionOfType(NoResultException.class)
                .isThrownBy(() -> commentsJdbcDao.getById(COMMENT_CORRECT_ID));

    }
}