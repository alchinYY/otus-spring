package ru.otus.spring.library.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.spring.library.domain.mongo.AuthorMng;
import ru.otus.spring.library.domain.mongo.BookMng;
import ru.otus.spring.library.domain.mongo.GenreMng;
import ru.otus.spring.library.domain.sql.Author;
import ru.otus.spring.library.domain.sql.Book;
import ru.otus.spring.library.domain.sql.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.library.job.CommonJobConfig.SQL_TO_MONGO_JOB;

@SpringBootTest
@SpringBatchTest
class CommonJobConfigTest {

    private static final int AUTHORS_LIST_SIZE = 5;
    private static final int GENRES_LIST_SIZE = 4;
    private static final int BOOKS_LIST_SIZE = 4;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JpaRepository<Author, Long> authorRepo;

    @Autowired
    private JpaRepository<Book, Long> bookRepo;

    @Autowired
    private JpaRepository<Genre, Long> genreRepo;

    @Autowired
    private MongoOperations mongoOperations;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void sqlTransferToMongo() throws Exception {

        List<Author> authorList = authorRepo.findAll();
        List<Book> bookList = bookRepo.findAll();
        List<Genre> genreList = genreRepo.findAll();

        assertThat(authorList).hasSize(AUTHORS_LIST_SIZE);
        assertThat(bookList).hasSize(GENRES_LIST_SIZE);
        assertThat(genreList).hasSize(BOOKS_LIST_SIZE);

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(SQL_TO_MONGO_JOB);

        JobParameters parameters = new JobParametersBuilder()
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        assertThat(mongoOperations.findAll(AuthorMng.class))
                .hasSize(AUTHORS_LIST_SIZE)
                .extracting(AuthorMng::getName)
                .contains(authorList.stream().map(Author::getName).collect(Collectors.toList()).toArray(String[]::new));

        assertThat(mongoOperations.findAll(GenreMng.class))
                .hasSize(GENRES_LIST_SIZE)
                .extracting(GenreMng::getName)
                .contains(genreList.stream().map(Genre::getName).collect(Collectors.toList()).toArray(String[]::new));

        assertThat(mongoOperations.findAll(BookMng.class))
                .hasSize(BOOKS_LIST_SIZE)
                .extracting(BookMng::getName)
                .contains(bookList.stream().map(Book::getName).collect(Collectors.toList()).toArray(String[]::new));
    }
}