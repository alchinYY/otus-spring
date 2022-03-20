package ru.otus.spring.library.service;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public interface CustomAclService {
    void createAcl(Long id, Class<?> aClass, Permission... permissions);
    Sid getOwnerByDomain(Class<?> aClass, Long id);
}
