package ru.otus.spring.library.integration.acl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.repo.BookRepo;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookAclRepoTest {
    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private MutableAclService aclService;

    @Test
    void getAll_WithoutAuth() {
        assertThatThrownBy(() -> bookRepo.findAll())
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    @WithMockUser(username = "admin")
    void getAll_allPermUser() {
        assertThat(bookRepo.findAll())
                .hasSize(4);
    }

    @Test
    @WithMockUser(roles = {"EDITOR"})
    void getAll_allSuperRole() {
        assertThat(bookRepo.findAll())
                .hasSize(4);
    }

    @Test
    @WithMockUser(username = "user1")
    void getAll_withPermOn2Book() {
        assertThat(bookRepo.findAll())
                .hasSize(2);
    }

    @Test
    @WithMockUser(roles = {"user2"})
    void getAll_withoutPerm() {
        assertThat(bookRepo.findAll())
                .isEmpty();
    }

    @Test
    @WithMockUser(username = "user1")
    void bookCreate_withoutRoleCreator() {
        Book book = new Book();
        book.setName("name");
        book.setGenre(new Genre(1L));
        book.setAuthors(Set.of(new Author(1L)));

        assertThatThrownBy(() -> bookRepo.save(book))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"BOOK_CREATOR"})
    void bookCreate_withRoleCreator() {
        Book book = new Book();
        book.setName("name");
        book.setGenre(new Genre(1L));
        book.setAuthors(Set.of(new Author(1L)));

        Book savedBook = bookRepo.save(book);

        assertThat(savedBook)
                .extracting(Book::getId)
                .isNotNull()
                .isNotEqualTo(0);
    }

    @Test
    @WithMockUser(username = "user1")
    void bookDelete_withoutPerm() {
        Book book = bookRepo.getById(1L);
        assertThatThrownBy(() -> bookRepo.delete(book))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "admin")
    void delete_withPerm() {

        Acl acl = aclService.readAclById(new ObjectIdentityImpl(Book.class, 1L));
        System.out.println(acl);

        bookRepo.deleteById(1L);

        assertThat(bookRepo.findAll())
                .extracting(Book::getId)
                .doesNotContain(1L);
    }
}
