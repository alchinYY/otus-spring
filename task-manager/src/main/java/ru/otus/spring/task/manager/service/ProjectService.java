package ru.otus.spring.task.manager.service;

import ru.otus.spring.task.manager.model.ProjectEntity;

public interface ProjectService {

    ProjectEntity getByKey(String key);

}
