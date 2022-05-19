package ru.otus.spring.task.manager.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.task.manager.exception.ProjectNotFoundException;
import ru.otus.spring.task.manager.model.ProjectEntity;
import ru.otus.spring.task.manager.repo.ProjectRepo;
import ru.otus.spring.task.manager.service.ProjectService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
@Import(ProjectServiceImpl.class)
class ProjectServiceImplTest {

    private static final String CORRECT_KEY = "HR";
    private static final String NOT_CORRECT_KEY = "HRTEST";


    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepo projectRepo;

    @Test
    void getByKey() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setKey(CORRECT_KEY);
        when(projectRepo.findByKey(CORRECT_KEY)).thenReturn(Optional.of(projectEntity));

        assertThat(projectService.getByKey(CORRECT_KEY))
                .isEqualTo(projectEntity);

    }

    @Test
    void getByKey_notFound() {
        when(projectRepo.findByKey(NOT_CORRECT_KEY)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProjectNotFoundException.class)
                .isThrownBy(() -> projectService.getByKey(NOT_CORRECT_KEY));
    }
}