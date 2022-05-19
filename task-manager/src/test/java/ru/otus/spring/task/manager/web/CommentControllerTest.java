package ru.otus.spring.task.manager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.CommentService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.resp.TaskCommentDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @MockBean
    private CommentService commentService;
    @MockBean
    private SecurityUtil securityUtil;
    @MockBean
    private ObjectMapperUtils objectMapperUtils;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void createComment() throws Exception {
        TaskCommentDto taskCommentDto = new TaskCommentDto();
        given(securityUtil.getCurrentUser()).willReturn(new UserEntity());
        given(objectMapperUtils.map(any(), eq(TaskCommentEntity.class))).willReturn(new TaskCommentEntity());
        willDoNothing().given(commentService).createComment(anyString(), any());

        mvc.perform(post(CommentController.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskCommentDto))
        )
                .andExpect(status().isOk());

    }
}
