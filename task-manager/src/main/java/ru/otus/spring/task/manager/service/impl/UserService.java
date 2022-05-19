package ru.otus.spring.task.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.spring.task.manager.exception.UserNotFoundException;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.repo.UserRepo;
import ru.otus.spring.task.manager.service.UserEntityService;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, UserEntityService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        return userRepo.save(userEntity);
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
    }
}