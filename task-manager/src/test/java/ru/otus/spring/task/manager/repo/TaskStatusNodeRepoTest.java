package ru.otus.spring.task.manager.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.task.manager.model.flow.TaskStatusNodeEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskStatusNodeRepoTest {

    private static final Long PROJECT_ID = 1L;
    private static final Long STATUS_TODO_ID = 1L;
    private static final String STATUS_TODO_NAME = "todo";

    @Autowired
    private TaskStatusNodeRepo taskStatusNodeRepo;

    @Test
    void getByNodeTaskStatusIdAndProjectId() {

        assertThat(taskStatusNodeRepo.getByNodeTaskStatusIdAndProjectId(STATUS_TODO_ID, PROJECT_ID))
                .isNotEmpty()
                .get()
                .extracting(node -> node.getNode().getName())
                .isEqualTo(STATUS_TODO_NAME);

    }
}