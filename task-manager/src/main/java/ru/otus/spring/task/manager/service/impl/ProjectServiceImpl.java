package ru.otus.spring.task.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.task.manager.exception.ProjectNotFoundException;
import ru.otus.spring.task.manager.model.ProjectEntity;
import ru.otus.spring.task.manager.repo.ProjectRepo;
import ru.otus.spring.task.manager.service.ProjectService;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;

    @Override
    public ProjectEntity getByKey(String key) {
        return projectRepo.findByKey(key)
                .orElseThrow(() -> new ProjectNotFoundException("project with key \"" + key + "\" not found"));
    }
}
