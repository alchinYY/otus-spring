package ru.otus.spring.task.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.task.manager.exception.UserNotFoundException;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.repo.UserRepo;
import ru.otus.spring.task.manager.service.UserEntityService;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, UserEntityService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return userRepo.save(userEntity);
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
    }
}
