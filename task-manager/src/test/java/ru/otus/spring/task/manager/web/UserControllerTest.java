package ru.otus.spring.task.manager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.impl.UserService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.UserDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private final static Long USER_ID = 1L;
    private final static String USER_LOGIN = "login";
    private final static String USER_PASSWORD = "1234";
    private final static String USER_EMAIL = "login@mail.com";

    @MockBean
    private UserService userService;
    @MockBean
    private ObjectMapperUtils objectMapperUtils;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void getById() throws Exception {

        UserDto userDto = new UserDto(USER_ID, USER_LOGIN, USER_EMAIL, USER_PASSWORD);

        given(userService.getById(anyLong())).willReturn(new UserEntity());
        given(objectMapperUtils.map(any(), any())).willReturn(userDto);

        mvc.perform(get(UserController.URL + "/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService, times(1)).getById(USER_ID);
        verify(objectMapperUtils, times(1)).map(any(), any());
    }

    @Test
    void create() throws Exception {
        UserDto userDto = new UserDto(USER_ID, USER_LOGIN, USER_EMAIL, USER_PASSWORD);

        given(objectMapperUtils.map(any(UserEntity.class), eq(UserDto.class)))
                .willReturn(userDto);
        given(objectMapperUtils.map(any(UserDto.class), eq(UserEntity.class)))
                .willReturn(mock(UserEntity.class));
        given(userService.create(any(UserEntity.class))).willReturn(mock(UserEntity.class));

        mvc.perform(
                post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService, times(1)).create(any());
        verify(objectMapperUtils, times(2)).map(any(), any());
    }
}

//    @PostMapping
//    public UserDto create(@RequestBody UserDto userDto) {
//        return objectMapperUtils.map(
//                userService.create(objectMapperUtils.map(userDto, UserEntity.class)),
//                UserDto.class);
//    }