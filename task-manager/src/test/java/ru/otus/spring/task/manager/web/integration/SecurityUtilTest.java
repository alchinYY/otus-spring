package ru.otus.spring.task.manager.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.impl.UserService;
import ru.otus.spring.task.manager.web.SecurityUtil;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(UserService.class)
class SecurityUtilTest {

    @Value("user")
    private String userNameForTest;
    @Value("password")
    private String userPass;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private UserService userService;

    @Test
    void getCurrentUser() {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(userService.loadUserByUsername(userNameForTest), userPass)
        );
        SecurityContextHolder.setContext(securityContext);
        UserEntity userEntity = securityUtil.getCurrentUser();
        assertThat(userEntity)
                .isNotNull()
                .extracting(UserEntity::getLogin)
                .isEqualTo(userNameForTest);
    }
}