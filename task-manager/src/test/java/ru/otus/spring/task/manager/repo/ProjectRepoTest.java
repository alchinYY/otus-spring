package ru.otus.spring.task.manager.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProjectRepoTest {

    private static final String CORRECT_KEY = "HR";
    private static final String NOT_CORRECT_KEY = "CUSTOM_KEY";

    @Autowired
    private ProjectRepo projectRepo;

    @Test
    void findByKey() {
        assertThat(projectRepo.findByKey(CORRECT_KEY))
                .isNotEmpty();
    }

    @Test
    void findByKey_notFound() {
        assertThat(projectRepo.findByKey(NOT_CORRECT_KEY))
                .isEmpty();
    }
}