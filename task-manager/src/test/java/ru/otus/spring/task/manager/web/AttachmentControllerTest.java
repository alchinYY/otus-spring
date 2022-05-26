package ru.otus.spring.task.manager.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.task.manager.exception.AttachNotFoundException;
import ru.otus.spring.task.manager.model.AttachmentEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.AttachFileService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttachmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AttachmentControllerTest {

    private static final Long ATTACH_ID = 1L;

    private static final String ATTACH_FILE_NAME = "attachment_test.txt";
    private static final String ATTACH_FILE_PATH = "src/test/resources/" + ATTACH_FILE_NAME;
    private static final String ATTACH_FILE_TEST_CONTENT = "test";

    @MockBean
    private AttachFileService attachFileService;
    @MockBean
    private SecurityUtil securityUtil;

    @Autowired
    private MockMvc mvc;

    @Test
    void loadFile() throws Exception {

        given(attachFileService.getById(ATTACH_ID))
                .willReturn(new AttachmentEntity(ATTACH_FILE_PATH, new UserEntity(), ATTACH_FILE_NAME));

        byte[] result = mvc.perform(get(AttachmentController.URL + "/{id}", ATTACH_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/octet-stream"))
                .andReturn().getResponse().getContentAsByteArray();

        assertThat(result)
                .asString().isEqualTo(ATTACH_FILE_TEST_CONTENT);

    }

    @Test
    void deleteFile() throws Exception {
        given(securityUtil.getCurrentUser()).willReturn(new UserEntity());
        willDoNothing().given(attachFileService).delete(eq(ATTACH_ID), any());

        mvc.perform(delete(AttachmentController.URL + "/{id}", ATTACH_ID))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFile_notFound() throws Exception {
        given(securityUtil.getCurrentUser()).willReturn(new UserEntity());
        willThrow(AttachNotFoundException.class).given(attachFileService).delete(eq(ATTACH_ID), any());

        mvc.perform(delete(AttachmentController.URL + "/{id}", ATTACH_ID))
                .andExpect(status().isNotFound());
    }
}