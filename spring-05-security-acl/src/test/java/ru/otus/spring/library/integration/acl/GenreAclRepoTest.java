package ru.otus.spring.library.integration.acl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.spring.library.repo.GenreJdbcDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class GenreAclRepoTest {
    @Autowired
    private GenreJdbcDao genreRepo;

    @Test
    void getAll_WithoutAuth() {
        assertThatThrownBy(() -> genreRepo.findAll())
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    @WithMockUser(username = "admin")
    void getAll_allPermUser() {
        assertThat(genreRepo.findAll())
                .hasSize(3);
    }

    @Test
    @WithMockUser(roles = {"EDITOR"})
    void getAll_allSuperRole() {
        assertThat(genreRepo.findAll())
                .hasSize(4);
    }

    @Test
    @WithMockUser(roles = {"user1"})
    void getAll_withoutPerm() {
        assertThat(genreRepo.findAll())
                .isEmpty();
    }
}
