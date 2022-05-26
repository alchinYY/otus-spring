package ru.otus.spring.task.manager.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AttachmentRepoTest {

    private final static Long ATTACH_STATUS_DELETED_ID = 1L;
    private final static Long ATTACH_STATUS_NOT_DELETED_ID = 2L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AttachmentRepo jpaRepository;

    @BeforeEach
    void setUp() {}

    @Test
    void findByIdAndStatusIsDeleted() {
        assertThat(jpaRepository.findByIdAndStatusIsNotDeleted(ATTACH_STATUS_DELETED_ID)).isEmpty();
    }

    @Test
    void findByIdAndStatusIsNotDeleted() {
        assertThat(jpaRepository.findByIdAndStatusIsNotDeleted(ATTACH_STATUS_NOT_DELETED_ID)).isNotEmpty();
    }
}