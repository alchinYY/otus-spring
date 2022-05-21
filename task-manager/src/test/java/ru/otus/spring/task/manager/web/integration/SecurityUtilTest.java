package ru.otus.spring.task.manager.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.impl.UserService;
import ru.otus.spring.task.manager.web.SecurityUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(UserService.class)
class SecurityUtilTest {

    @Value("user")
    private String userNameForTest;
    @Value("password")
    private String userPass;
    @Autowired
    private SecurityUtil securityUtil;
    @MockBean
    private UserService userService;

    @Test
    void getCurrentUser() {
        UserEntity userEntityForTest = new UserEntity();
        userEntityForTest.setLogin(userNameForTest);
        userEntityForTest.setPassword(new BCryptPasswordEncoder().encode(userPass));
        when(userService.loadUserByUsername(userNameForTest)).thenReturn(userEntityForTest);

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