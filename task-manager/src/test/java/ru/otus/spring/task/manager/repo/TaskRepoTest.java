package ru.otus.spring.task.manager.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.task.manager.model.TaskEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepoTest {

    private static final String TEST_TASK_KEY = "HR-1";

    @Autowired
    private TaskRepo taskRepo;

    @BeforeEach
    void setUp() {}

    @Test
    void findByKey() {
        Optional<TaskEntity> taskEntityOptional = taskRepo.findByKey(TEST_TASK_KEY);
        assertThat(taskEntityOptional).isNotEmpty();

        assertThat(taskEntityOptional.get().getAttachments())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void findAllEntityByKey() {
        Optional<TaskEntity> taskEntityOptional = taskRepo.findAllEntityByKey(TEST_TASK_KEY);
        assertThat(taskEntityOptional).isNotEmpty();

        assertThat(taskEntityOptional.get().getAttachments())
                .isNotEmpty()
                .hasSize(2);
    }
}